if [ "$1" == "" ]
then
  echo "Usage: . start.sh dev|test"
  echo "Exiting..."
else
  docker-compose run --rm $1 bash
  # docker-compose run --rm grails /grails/bin/grails test-app
fi
