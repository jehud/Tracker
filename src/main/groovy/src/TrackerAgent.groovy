#!/usr/bin/groovy


println 'Starting Metric Agent'


new File ('config/agent.conf').eachLine
{
  line ->
  pattern = /^log.watch\d+=(.*)$/
  line.eachMatch(pattern)
  {
    //locate the services and directories to watch
    match -> println match[1]
  }
}
