#!/bin/sh

./bin/project-name -Dhttp.address=`hostname -f` -Dhttp.port=9000 -Dpidfile.path=RUNNING_PID1 -Dlogger.file=/opt/projects/config/logger.xml -Dconfig.file=/opt/projects/fqa/config/external-project.conf -J-javaagent:/opt/software/newrelic/newrelic.jar -Dnewrelic.environment=<env> -mem 2048 >> this-application.out &