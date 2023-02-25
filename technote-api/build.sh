#!/bin/sh

cd /Users/leon/Github/suzaku/technote-api

mvn clean package -Dmaven.test.skip=true

scp -P 4509 target/cms-0.0.1-SNAPSHOT.jar root@suzaku:/suzaku/target/suzaku.jar

cd /Users/leon/Github/suzaku/technote-ui

npm run./ build --production

tar czvf dist.tar.gz dist

scp -P 4509 dist.tar.gz root@suzaku:/suzaku/target/dist.tar.gz

ssh -p 4509 root@suzaku "/bin/bash -c /suzaku/bin/restart.sh"

