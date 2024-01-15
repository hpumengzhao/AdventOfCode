import re
from z3 import Int, Solver
#LOWER = 7
#UPPER = 27

LOWER = 200000000000000
UPPER = 400000000000000

def checkXYcollide(px1, py1, vx1, vy1, px2, py2, vx2, vy2):
        
    x1_lower = LOWER - px1
    x1_upper = UPPER - px1
    y1_lower = LOWER - py1
    y1_upper = UPPER - py1

    x2_lower = LOWER - px2
    x2_upper = UPPER - px2
    y2_lower = LOWER - py2
    y2_upper = UPPER - py2

    basefrac = vx1 * vy2 - vy1 * vx2

    if basefrac == 0:
        return False   
    
    basesub1 = px2 - px1
    basesub2 = py2 - py1

    t2 = (vy1 * (px2 - px1) - vx1 * (py2 - py1)) / basefrac
    t1 = (vy2 * (px2 - px1) - vx2 * (py2 - py1)) / basefrac

    if t1 < 0 or t2 < 0:
        return False
    
    ok = True

    # check x1
    maxx = max(basefrac * x1_lower, basefrac * x1_upper)
    minn = min(basefrac * x1_lower, basefrac * x1_upper)
    ok &= (minn <= vx1 * vy2 * basesub1 - vx1 * vx2 * basesub2)
    ok &= (maxx >= vx1 * vy2 * basesub1 - vx1 * vx2 * basesub2)

    # check x2
    maxx = max(basefrac * x2_lower, basefrac * x2_upper)
    minn = min(basefrac * x2_lower, basefrac * x2_upper)
    ok &= (minn <= vx2 * vy1 * basesub1 - vx1 * vx2 * basesub2)
    ok &= (maxx >= vx2 * vy1 * basesub1 - vx1 * vx2 * basesub2)

    # check y1
    maxx = max(basefrac * y1_lower, basefrac * y1_upper)
    minn = min(basefrac * y1_lower, basefrac * y1_upper)
    ok &= (minn <= vy1 * vy2 * basesub1 - vy1 * vx2 * basesub2)
    ok &= (maxx >= vy1 * vy2 * basesub1 - vy1 * vx2 * basesub2)

    # check y2
    maxx = max(basefrac * y2_lower, basefrac * y2_upper)
    minn = min(basefrac * y2_lower, basefrac * y2_upper)
    ok &= (minn <= vy1 * vy2 * basesub1 - vx1 * vy2 * basesub2)
    ok &= (maxx >= vy1 * vy2 * basesub1 - vx1 * vy2 * basesub2)

    return ok

def readInput():
    px = []
    py = []
    pz = []
    vx = []
    vy = []
    vz = []
    file_path = "../resources/day24.in"

    with open(file_path, 'r') as file:
        for line in file:
            tokens = re.split(r"[,\s@]+", line.strip())
            px.append(int(tokens[0]))
            py.append(int(tokens[1]))
            pz.append(int(tokens[2]))
            vx.append(int(tokens[3]))
            vy.append(int(tokens[4]))
            vz.append(int(tokens[5]))
    return px, py, pz, vx, vy, vz


def solveTask2(px, py, pz, vx, vy, vz):
    cx = Int('cx')
    cy = Int('cy')
    cz = Int('cz')
    vx_var = Int('vx_var')  
    vy_var = Int('vy_var')  
    vz_var = Int('vz_var') 

    s = Solver()
    n = len(px)
    for i in range(0, n):
        s.add((cx - px[i]) * (vy_var - vy[i]) == (cy - py[i]) * (vx_var - vx[i]))
        s.add((cx - px[i]) * (vz_var - vz[i]) == (cz - pz[i]) * (vx_var - vx[i]))
        s.add((cy - py[i]) * (vz_var - vz[i]) == (cz - pz[i]) * (vy_var - vy[i]))
    print(s.check())

    model = s.model()
    sum = 0

    sum += model[cx].as_long()
    sum += model[cy].as_long()
    sum += model[cz].as_long()
    print(sum)


## main
px, py, pz, vx, vy, vz = readInput()


## Task 1
n = len(px)
res = 0
for i in range(0, n):
    for j in range(i + 1, n):
        if checkXYcollide(px[i], py[i], vx[i], vy[i], px[j], py[j], vx[j], vy[j]):
            res += 1

print(res)

solveTask2(px, py, pz, vx, vy, vz)