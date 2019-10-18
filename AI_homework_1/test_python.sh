#!/usr/bin/env bash 
counter=0

for i in {1..50}
do 
	echo "Running: $i"
	
	rm -rf input.txt output.txt
	cp testcases/input$i.txt ./input.txt

	java homework.java
	
	RES=$(python3 test.py --input input.txt --output output.txt --answers testcases/output$i.txt)
	if [[ $RES == "True" ]]; then
		counter=$(($counter+1))
	else
		echo "$RES"
	fi

done
echo $counter
echo "All done!!"
rm -rf input.txt output.txt
