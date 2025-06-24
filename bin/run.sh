#!/bin/sh

usage() \
{
    echo
    echo "Usage: $0 [options] <arg>"
    echo
    echo " Where options are:"
    echo
    echo "   -h             - This help"
    echo
    exit 1
}

file=`readlink -f $0`
basedir=$(dirname `dirname $file`)
libs=`find $basedir/libs -type f | xargs | tr ' ' ':'`

EXTRAS=""
while getopts "h" arg; do
  case $arg in
    h) usage
        ;;
    *) usage
        ;;
  esac
done

shift $((OPTIND-1))

if [ $# -ne 1 ]
then
    usage
fi

export CLASSPATH=$libs:$basedir/conf
java $EXTRAS yourpackage.Main "$@"
