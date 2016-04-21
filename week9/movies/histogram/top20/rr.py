import matplotlib.pyplot as plt
import sys
text_file = open(sys.argv[1], "r")
lines = text_file.read().strip().split(',')

T2 = [ int(x.strip()) for x in lines ]
text_file.close()

n,bins,patches = plt.hist(T2, bins=[x for x in range(0,51)],facecolor='green', alpha=0.75,label=[sys.argv[1]])
axes = plt.gca()

axes.set_ylim([0,350])
plt.grid(True)
plt.xlabel('no of weeks')
plt.ylabel('no of Books')
plt.legend()
plt.savefig(str(sys.argv[1])+".jpg")

