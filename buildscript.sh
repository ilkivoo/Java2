#!/bin/bash
files=$(find . -maxdepth 1 -type d | grep "./0.*")
for file in $files
do
	cd $file
	mvn test -B
	cd ../
done
	
