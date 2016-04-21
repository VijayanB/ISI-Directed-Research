import matplotlib.pyplot as plt
import sys
text_file = open(sys.argv[1], "r")
lines = text_file.read().strip().split(',')

T2 = list()
for x in lines:
	 num = int(x.strip())
	 if num != 20 :
                T2.append(num)
text_file.close()

n,bins,patches = plt.hist(T2, bins=[x for x in range(0,max(T2)+1)],facecolor='green', alpha=0.75,label=[sys.argv[2]])
plt.grid(True)
plt.xlabel('no of weeks')
plt.ylabel('no of songs')
plt.legend()
plt.savefig(sys.argv[2])

