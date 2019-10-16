input1 = 'output.txt'
input2 = 'output_ref_BFS.txt'

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
            if len(res1[i]) == len(res2[i]):
                print("Matching: line " + str(i) + ' with path length ' + str(len(res1[i])))
            else:
                print("FAILURE: line " + str(i) + ' with path length of ' + str(input1) + ': ' + str(len(res1[i])) + ' and path length of ' + str(input2) + ': ' + str(len(res2[i])))
