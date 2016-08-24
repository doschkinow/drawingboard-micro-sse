This is the drawingboard-micro-sse example based only on Grizzly+Jersey.
Together with drawingboard-micro-websocket-nodejs it delivers the functionality of 
drawingboard-light as a set of microservices that can be redeployed separately

environment variable WS_LOCATION points to the deployment url of 
drawingboard-micro-websocket-nodejs and can be edited in accs/deployment.json

to build: "mvn package"
to run: "mvn build" or "java -jar target/drawingboard-micro-sse-1.0-SNAPSHOT-jar-with-dependencies.jar"

to deploy to ACCS:
    go in the accs directory
    edit the variables for credentials and identity domain in rest.sh
    run rest.sh e.g. as "bash -x rest.sh create"

