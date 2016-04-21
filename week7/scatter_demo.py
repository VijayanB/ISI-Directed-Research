"""
Simple demo of a scatter plot.
"""
import numpy as np
import matplotlib.pyplot as plt
import sys

tup =(0,0)
x= list()
y= list()
text_file = open(sys.argv[1], "r")
for line in text_file:
	inp = line.strip().split(",")
	x.append(int(inp[0]))
	y.append(int(inp[1]))
    


plt.xlabel('Lowest Rank ( worst )')
plt.ylabel('highest Rank ( Peak position)')
plt.axis([1, 100, 1, 100])
plt.scatter(x, y)
plt.title(sys.argv[3])
plt.savefig(sys.argv[2])
