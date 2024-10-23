
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
    
3. Deploy helpers

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

4. TO BE CONTINUED...
