Updates from previous Submission:
>> Updated PriceHike Logic: converts everything to cents and computes
>> Removed BigDecimal: was slow and unable to execute test6 properly
>> Allocating above -Xms4g, we observed significant speedup in run time

---------------------------- READ ME --------------------------

This is a readme file containing instructions for executing code for 
Long Project 3: Multi-Dimensional search

Team No: 39

@Authors:
Pranita Hatte: prh170230
Prit Thakkar: pvt170000
Shivani Thakkar: sdt170030
Yash Pradhan: ypp170130

Long Project 3: Multi-Dimensional search
Uses balanced trees(TreeMap) and HashMap to organize the items in inventory for efficient search, insert, update and delete operations

Instructions to execute code:

The uploaded folder with name as my net id: ypp170130 contains java files "MDS.java".

Steps for running code from cmd prompt:

1. Compile the MDS.java by executing the following command
> javac ypp170130/MDS.java

2. Compile and run the driver
> javac ypp170130/TLP3Driver.java
> java -Xss512m -Xms6g ypp170130/TLP3Driver PATH_INPUT_FILE

Example:

java -Xss512m -Xms6g ypp170130/TLP3Driver input/lp3-t6.txt

Sample Output:
14070859