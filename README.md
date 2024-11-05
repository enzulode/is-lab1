
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

4. TO BE CONTINUED...
