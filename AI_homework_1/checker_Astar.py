mapfile = 'input.txt'
input1 = 'output.txt'
input2 = 'output_reference.txt'

def read_file(fn):
    line_count = 0
    with open(fn) as f:
        global mode, W, H, landing, maxDiff, numTarget, targets, space, visited, parents, costs

        for line in f:
            # print(line)
            if line_count == 0:
                mode = line.split()[0]
            elif line_count == 1:
                W = int(line.split()[0])
                H = int(line.split()[1])
            elif line_count == 2:
                landing = (int(line.split()[0]), int(line.split()[1]))
            elif line_count == 3:
                maxDiff = int(line)
            elif line_count == 4:
                numTarget = int(line)
            elif 5 <= line_count < 5 + numTarget:
                targets.append((int(line.split()[0]), int(line.split()[1])))
            else:
                space.append([int(i) for i in line.split()])

            line_count = line_count + 1

def cost(pos1, pos2):
    global space
    pos1 = pos1.split(',')
    pos2 = pos2.split(',')
    if pos1[0] == pos2[0] or pos1[1] == pos2[1]:
        return 10+abs(space[int(pos1[1])][int(pos1[0])] - space[int(pos2[1])][int(pos2[0])])
    return 14+abs(space[int(pos1[1])][int(pos1[0])] - space[int(pos2[1])][int(pos2[0])])

def pathCost(path):
    res = 0
    curr = path[0]

    for i in range(1,len(path)):
        res += cost(curr, path[i])
        curr = path[i]
    return res

mode = ''
W = 0
H = 0
landing = (0, 0)
maxDiff = 0
numTarget = 0
targets = []
space = []
visited = []
parents = []
costs = []

read_file(mapfile)

lc1 = 0
res1 = []
with open(input1) as f1:
    for i,res in enumerate(f1):
        lc1 = lc1+1
        r = res.strip().split(' ')
        if len(r)==0:
            print('Failure on ' + str(input1) + ' line: ' + str(i+1)+ ' NO OUTPUT')
        res1.append(r)

lc2 = 0
res2 = []
with open(input2) as f2:
    for i,res in enumerate(f2):
        lc2 = lc2+1
        r = res.strip().split(' ')
        if len(r)==0:
            print('Failure on ' + str(input2) + ' line: ' + str(i+1)+ ' NO OUTPUT')
        res2.append(r)

if lc1 != lc2:
    print('Failure: total lines does not match')
else:
    for i in range(lc1):
        if res1[i][0] == 'FAIL' or res2[i][0] == 'FAIL':
            if res1[i][0] == 'FAIL' and res2[i][0] == 'FAIL':
                print("Matching: line " + str(i) + ' cannot reach target')
            else:
                print("FAILURE: line " + str(i) + ' one output cannot reach target')
        else:
            pc1 = pathCost(res1[i])
            pc2 = pathCost(res2[i])
            if pc1 == pc2:
                print("Matching: line " + str(i) + ' with path length ' + str(pc1))
            else:
                print("FAILURE: line " + str(i) + ' with path cost of ' + str(input1) + ': ' + str(pc1) + ' and path cost of ' + str(input2) + ': ' + str(pc2))
