#!/bin/bash

BIN_DIR=$(dirname $(which $0))
#APP_NAME=TRACKER_AGENT
APP_NAME=iChatAgent

function start
{
    if [ isRunning == 1 ]
    then
        echo "Agent is already running"
        exit 0
    fi
    echo "Starting Tracker agent"
    # do start thing
}

function stop
{
    if [ ! isRunning == 1 ]
    then
        echo "Agent is not running"
        exit 0
    fi

    # standard PID collector
    PID=`ps aux | grep $APP_NAME | grep -v grep | awk '{print $2}'`
    echo "Killing pid $PID cleanly with -15"
    kill -15 $PID
    # if pkill exists
    #pkill -f $APP_NAME
}

function isRunning
{
    if [ "$(ps aux | grep $APP_NAME | grep -v grep)" ]
    then
        return 1
    else
        return 0
    fi
}

# use to set classpath eventually
#JAR_LIST=`ls $JAR_DIR/*.jar`
#JARS=`echo JAR_LIST | sed "s/ /:/g"`

# set the command
COMMAND=$1

if [ -z $COMMAND ]
then
  echo "agent.sh start|stop"
  exit 0
fi

echo "COMMAND=$COMMAND"

if [ $COMMAND == "start" ]
then
    start
elif [ $COMMAND == "stop" ]
then
    stop
fi