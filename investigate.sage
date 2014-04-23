
K = 73

_l = [0, 0, 0, 1, 0, 2, 3, 4, 0]
def sg_x(i):
    if i < len(_l):
        return _l[i]
    else:
        return i-4

kk = []
for i in Partitions(K, max_slope=-1, min_length=2, max_length=4):
    kk.append((i, map(lambda x:sg_x(x), i), reduce(lambda x,y:x^^sg_x(y), i, 0)))

kk.sort(key=lambda k:k[2])

kf = filter(lambda k: k[2] in k[1], kk)

