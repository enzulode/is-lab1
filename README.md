
## Deployment

1. Make kubernetes cluster up & running

    The kubernetes cluster set up is outside the scope of this document. No matter what
    kind of cluster you are running this application on.

2. Set up the external database

    The application is designed to use the database, which is running outside 
    the k8s cluster. Due to the security policies, followed on the database server the
    DBMS (postgresql in our case) is only accessible from the local machine (from the server
    where it's actually deployed).
    
    For that reason we have to set up the SSH tunnel, which can forward ports from the
    database server on the kubernetes node and make our backend applications running in
    the mentioned k8s cluster able to access the remote database.
    
    First of all, let's start from the ssh keys generation
    ```bash
    mkdir keys
    ssh-keygen -o -a 100 -t ed25519 -f keys/<key_name>
    ``` 
    Now we have the keypair at `keys` directory. Please, copy generated public key to the remote database server.
    
    The next step: create the kubernetes cluster secret with the generated keys
    and all necessary data.
    ```bash
    kubectl create namespace postgres-ssh
    kubectl create secret generic pg-ssh-keys -n postgres-ssh \
      --from-file=private-key=./keys/<key_name>

    kubectl create secret generic pg-ssh-info -n postgres-ssh \
      --from-literal=ssh-hostuser=<username> \
      --from-literal=ssh-hostname=<hostname> \
      --from-literal=ssh-port=<port>
    
    kubectl create namespace dev
    kubectl label namespace dev helios-pg=allow
    kubectl create secret generic pg-ssh-creds -n dev \
      --from-literal=pg-user=<username> \
      --from-literal=pg-pass=<password> \
      --from-literal=pg-database=<database>
    ```

    Apply several configurations to make remote postgresql database accessible
    within your k8s cluster using the FQDN: deploy the ssh-tunneling container 
    that helps us to forward ports from the remote server, special service which 
    exactly makes FQDN based requests to the database possible and the network 
    policy which is just the security policy which does not allow every container
    running in the cluster access the remote database.

    ```bash
    kubectl apply -f ./k8s/postgres-ssh-tunnel-deployment.yaml
    kubectl apply -f ./k8s/postgres-ssh-tunnel-service.yaml
    kubectl apply -f ./k8s/postgres-ssh-tunnel-networkpolicy.yaml
    ```

3. Setup PostgreSQL database for the Keycloak

    Prepare the persistence volume claim for the database with
    ```bash
    kubectl apply -f ./k8s/keycloak-db-namespace.yaml
    kubectl apply -f ./k8s/keycloak-db-storage-class.yaml
    kubectl apply -f ./k8s/keycloak-db-pv.yaml
    kubectl apply -f ./k8s/keycloak-db-pvc.yaml
    ```

    We have to generate development CA for future TLS certificates signing. Execute 
    following commands to make this happen.
    ```bash
    openssl genrsa -des3 -out keys/<dev_CA_name>.key 2048
    openssl req -x509 -new -nodes -key keys/<dev_CA_name>.key -sha256 -days 1825 -out keys/<dev_CA_name>.pem
    ```
    After that install the generated CA in your system.
    
    And then generate TLS certificate for the PostgreSQL
    ```bash
    openssl genrsa -out keys/<tls_cert_key>.key 2048
    openssl req -new -key keys/<tls_cert_key>.key -out keys/<tls_cert_req>.csr
    openssl x509 -req -sha256 -days 825 -CAcreateserial \
      -CA keys/<dev_CA_name>.pem \
      -CAkey keys/<dev_CA_name>.key \
      -in keys/<tls_cert_req>.csr \
      -out keys/<tls_cert_name>.crt
    ```
    
    Create k8s secret that stores tls certificates for the PostgreSQL
    ```bash
    kubectl create secret tls keycloak-postgresql-tls -n keycloak-db \
      --key=keys/<tls_cert_key>.key \
      --cert=keys/<tls_cert_name>.crt
    ```
    
    Now we have to deploy PostgreSQL using helm
    ```bash
    helm install dev bitnami/postgresql --namespace keycloak-db \
      --set primary.persistence.existingClaim=<postgresql_pvc_name> \
      --set auth.postgresPassword=<postgresql_password> \
      --set volumePermissions.enabled=true \
      --set tls.enabled=true \
      --set tls.certificatesSecret=keycloak-postgresql-tls \
      --set tls.certFilename=tls.crt \
      --set tls.certKeyFilename=tls.key
    ```
   
    Connect to the database (e.g. through the IDEA database integration) and create
    separate database for the keycloak.
    
    ```postgresql
    CREATE ROLE keycloak_database_username WITH LOGIN ENCRYPTED PASSWORD 'keycloak_database_password';
    CREATE DATABASE keycloak_db OWNER keycloak_database_username;
    ```
    
    Replace `keycloak_database_username`, `keycloak_database_password`, `keycloak_db` with
    desired values.

4. Setup Keycloak

    Create the keycloak namespace using
    ```bash
    kubectl apply -f ./k8s/keycloak-namespace.yaml
    ```
    
    Create k8s secret for the generated CA.
    ```bash
    kubectl create secret generic keycloak-postgresql-tls-ca -n keycloak \
      --from-file=ca.crt=keys/<dev_CA_name>.pem
    ```
    
    Create k8s secret with external database configuration
    ```bash
    kubectl create secret generic keycloak-external-postgresql-cfg -n keycloak \
      --from-literal=external-host=<external_db_FQDN> \
      --from-literal=external-port=<external_db_port> \
      --from-literal=external-user=<external_db_user> \
      --from-literal=external-database=<external_database> \
      --from-literal=external-password=<external_database_password>
    ```
    
    Create k8s secret with keycloak admin credentials
    ```bash
    kubectl create secret generic keycloak-admin-password -n keycloak \
      --from-literal=password=admin
    ```
    
    Generate TLS certificate for the keycloak SSO ingress
    ```bash
    openssl genrsa -out keys/<sso_tls>.key 2048
    openssl req -new \
      -key keys/<sso_tls>.key \
      -out keys/<sso_tls>.csr
    openssl x509 -req -sha256 -days 825 -CAcreateserial \
          -CA keys/<dev_CA_name>.pem \
          -CAkey keys/<dev_CA_name>.key \
          -in keys/<sso_tls>.csr \
          -out keys/<sso_tls>.crt
    ```
    
    And for Admin ingress
    ```bash
    openssl genrsa -out keys/<admin_tls>.key 2048
    openssl req -new \
      -key keys/<admin_tls>.key \
      -out keys/<admin_tls>.csr
    openssl x509 -req -sha256 -days 825 -CAcreateserial \
          -CA keys/<dev_CA_name>.pem \
          -CAkey keys/<dev_CA_name>.key \
          -in keys/<admin_tls>.csr \
          -out keys/<admin_tls>.crt
    ```
    
    And now create k8s secrets for the generated TLS certificates
    ```bash
    kubectl create secret tls sso-keycloak-local-tls -n keycloak \
      --key=keys/<sso_tls>.key \
      --cert=keys/<sso_tls>.crt
    ```
    ```bash
    kubectl create secret tls admin-keycloak-local-tls -n keycloak \
      --key=keys/<admin_tls>.key \
      --cert=keys/<admin_tls>.crt
    ```
    
    Deploy keycloak with the external PostgreSQL database and enforced
    SSL/TLS for the DB interconnections.
    ```bash
    helm install dev bitnami/keycloak --namespace keycloak -f ./k8s/keycloak-values-helm.yaml
    ```

5. TO BE CONTINUED...
