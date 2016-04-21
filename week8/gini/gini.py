import matplotlib.pyplot as plt
import numpy as np
import sys
from numpy import *

text_file = open(sys.argv[1], "r")
input1 = dict()
x = np.array([])
y = np.array([])
for line in text_file:
	lines = line.strip().split(',')
  	if len(lines) > 1 :
        	input1[(lines[0].strip())] = lines[1].strip()
i = 0
my_xticks = []
for key in sorted(input1):
       	my_xticks.append(key)
	x = append(x,[i])
	y = append(y,[input1[key]])
 	i = i + 1


#plt.xticks(x, my_xticks)
#plt.xlim(1950, 2011)
plt.plot(x, y)
plt.xlabel('Year')
plt.ylabel('Gini Coefficient')
plt.savefig(sys.argv[2])