def mex(L):
	if not L:
		return 0

	L = sorted(list(set(L)))

	i = 0
	while True:
		if len(L) <= i or L[i] != i:
			return i
		i += 1

def nim_sum(L):
	if not L:
		return 0
	return nim_sum(L[:-1])^^L[-1]


@cached_function
def superGrundy(n, M=Infinity, report=False):
	if n in [0,1,2]:
		return 0

	followSG = []
	for part in Partitions(n, max_slope=-1, min_length=2, max_length=M):
		if report:
			print ">>>", n, part, nim_sum(map( lambda x : superGrundy(x,M), part ))
		followSG.append(nim_sum(map( lambda x : superGrundy(x,M), part )))

	if report:
		print "Follows for", n, followSG

	return mex(followSG)

@cached_function
def superGrundyFixedLen(n, M=2, report=False):
	if n in [0,1,2]:
		return 0

	followSG = []
	for part in Partitions(n, max_slope=-1, length=M):
		if report:
			print ">>>", n, part, nim_sum(map( lambda x : superGrundyFixedLen(x,M), part ))
		followSG.append(nim_sum(map( lambda x : superGrundyFixedLen(x,M), part )))

	if report:
		print followSG

	return mex(followSG)



if __name__ == "__main__":
    import sys
    MAX_CALCULATION = 60
    REPORT = False
    if len(sys.argv) < 3:
        print "Usage:", sys.argv[0], "<mode> <M>"
        print "    where mode is 0 for any split and 1 for fixed split"
        print "    and M is the maximum split"
        sys.exit(0)

    if int(sys.argv[2]) < 2:
        print "M too small:", sys.argv[2], "Must be at least 2."
        sys.exit(0)

    if int(sys.argv[1]) == 0:
        print "Calculating SG values for any split with M =", sys.argv[2]
        i = 0
        while i < MAX_CALCULATION:
            print superGrundy(i, int(sys.argv[2]), REPORT)
            i += 1
            sys.stdout.flush()

    elif int(sys.argv[1]) == 1:
        print "Calculating SG values for fixed split with M =", sys.argv[2]
        i = 0
        while i < MAX_CALCULATION:
            print superGrundyFixedLen(i, int(sys.argv[2]), REPORT)
            i += 1
            sys.stdout.flush()



























