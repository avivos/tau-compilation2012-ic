
echo off
START ant libparser
ant parser
START ant scanner
START ant rebuild

echo finished