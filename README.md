
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

3. Setup Keycloak with PostgreSQL database

    We are going to use bitnami helm chart with keycloak. Firstly, we have to create 
    our TLS certificates and some other data for the Keycloak. So let's make in done.
    
    The following set of commands will generate us a self-signed TLS certificate and
    add this certificate as the k8s secret.
    ```bash
    openssl genrsa -out keys/keycloak.key 2048
    openssl req -new -key  keys/keycloak.key -out  keys/keycloak.csr
    openssl x509 -req -in keys/keycloak.csr -signkey keys/keycloak.key -out keys/keycloak.crt -days 365
    kubectl create secret tls <keycloak_hostname>-tls \
      --cert=keys/<cert_name>.crt \
      --key=keys/<cert_key_name>.key \
      --create-namespace \
      --namespace keycloak
    ```
    
    Now we have to create secrets for various keycloak-related credentials
    ```bash
    kubectl create secret generic keycloak-admin-password \
      -n keycloak \
      --from-literal=password=<admin_password>
    ```
    
    Let's generate TLS cert to secure Keycloak connections within the cluster.
    In order to do that we have to create another tls secret for our cluster.
    ```bash
    openssl genrsa -out keys/keycloak_internal.key 2048
    openssl req -new -key keys/keycloak_internal.key -out keys/keycloak_internal.csr
    openssl x509 -req -in keys/keycloak_internal.csr -signkey keys/keycloak_internal.key -out keys/keycloak_internal.crt -days 365
    kubectl create secret tls keycloak-internal-tls \
      --cert=keys/<internal_cert_name>.crt \
      --key=keys/<internal_cert_key_name>.key \
      --namespace keycloak
    ```
    That TLS certificate with be used on the cluster-wide connections to the 
    Keycloak. In the future another similar secret will be created in the
    application-containing namespace (`dev` in our case).
    
    Now use `helm` to install the keycloak alongside with postgresql
    ```bash
    helm install dev-keycloak bitnami/keycloak --namespace keycloak -f k8s/keycloak-and-pg-helm-values.yaml
    ```

4. TO BE CONTINUED...
