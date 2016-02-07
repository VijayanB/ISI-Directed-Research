import matplotlib.pyplot as plt
import numpy as np
import sys
from numpy import *
file_list = sys.argv[1].split(',')
plt.gca().set_color_cycle(['red', 'green', 'blue', 'yellow'])
legend1 = []
for ff in file_list:
        temp = ff.split('_')
        legend1.append(str(temp[len(temp)-1]))
	text_file = open(ff, "r")
	input1 = dict()
	x = np.array([])
	y = np.array([])
	for line in text_file:
		lines = line.strip().split(',')
  		if len(lines) > 1 :
        		input1[int(lines[0].strip())] = lines[1].strip()
	i = 0
	my_xticks = []
	for key in sorted(input1):
       		my_xticks.append(key)
		x = append(x,[i])
		y = append(y,[input1[key]])
 		i = i + 1


	plt.xticks(x, [])
	plt.plot(x, y)
plt.legend(legend1)
plt.grid(True)
plt.xlabel(sys.argv[3])
plt.ylabel(sys.argv[4])
plt.savefig(sys.argv[2])
	

