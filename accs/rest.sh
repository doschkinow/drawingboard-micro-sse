app=dbmicrosse
onejar=drawingboard-micro-sse-1.0-SNAPSHOT-jar-with-dependencies.jar
notes="$app for ACCS"
credentials="cloud.admin:mOuSY@2NotIOn"
identityDomain=gse00011011
#credentials="peter.doschkinow@oracle.com:***"

#endpoint=https://apaas.us.oraclecloud.com/paas/service/apaas/api/v1.1/apps
endpoint=https://apaas.europe.oraclecloud.com/paas/service/apaas/api/v1.1/apps

case $1 in
create)
    # create app archive
    tar -czvf $app.tar.gz manifest.json -C ../target $onejar
    # create container
    curl -v -i -X PUT -u $credentials \
    https://$identityDomain.storage.oraclecloud.com/v1/Storage-$identityDomain/$app
    # put archive in storage container
    curl -v -i -X PUT -u $credentials \
        https://$identityDomain.storage.oraclecloud.com/v1/Storage-$identityDomain/$app/$app.tar.gz \
        -T $app.tar.gz
    # create app
    curl -v -X POST -u $credentials \
        -H "X-ID-TENANT-NAME:$identityDomain" \
        -H "Content-Type: multipart/form-data" \
        -F "name=$app" -F "runtime=java" -F "subscription=Monthly" \
        -F "deployment=@deployment.json" \
        -F "archiveURL=$app/$app.tar.gz" -F "notes=$notes"  \
        $endpoint/$identityDomain
    rm $app.tar.gz
    ;;

update)
    tar -czvf $app.tar.gz manifest.json -C ../target $onejar
    # put archive in storage container
    curl -v -i -X PUT -u $credentials \
        https://$identityDomain.storage.oraclecloud.com/v1/Storage-$identityDomain/$app/$app.tar.gz \
        -T $app.tar.gz
    curl -X PUT -u $credentials \
        -H "X-ID-TENANT-NAME:$identityDomain" \
        -H "Content-Type: multipart/form-data" \
        -F "deployment=@deployment.json" \
        -F "archiveURL=$app/$app.tar.gz" -F "notes=$notes"  \
        $endpoint/$identityDomain/$app
    rm $app.tar.gz
    ;;

update-config)
    curl -X PUT -u $credentials \
        -H "X-ID-TENANT-NAME:$identityDomain" \
        -H "Content-Type: multipart/form-data" \
        -F "deployment=@deployment.json" \
        -F "notes=$notes"  \
        $endpoint/$identityDomain/$app
    ;;

delete)
    curl -v -X DELETE -u $credentials \
        -H "X-ID-TENANT-NAME:$identityDomain" \
        $endpoint/$identityDomain/$app
    ;;
esac