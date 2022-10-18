package dm.utils;

public class Permuter {
        private int[] perms;

        private int[] indexPerms;

        private int[] directions;

        private int[] iSwap;

        private int N;

        private int movingPerm = N;


        static int FORWARD = +1;

        static int BACKWARD = -1;

        public Permuter(int N) {
            this.N = N;
            perms = new int[N];
            indexPerms = new int[N];
            directions = new int[N];
            iSwap = new int[N];

            for (int i = 0; i < N; i++) {
                directions[i] = BACKWARD;
                perms[i] = i;
                indexPerms[i] = i;
                iSwap[i] = i;
            }

            movingPerm = N;
        }

        public int[] getNext() {
            do {
                if (movingPerm == N) {
                    movingPerm--;
                    return perms;
                } else if (iSwap[movingPerm] > 0) {
                    int swapPerm = perms[indexPerms[movingPerm] + directions[movingPerm]];
                    perms[indexPerms[movingPerm]] = swapPerm;
                    perms[indexPerms[movingPerm] + directions[movingPerm]] = movingPerm;
                    indexPerms[swapPerm] = indexPerms[movingPerm];
                    indexPerms[movingPerm] = indexPerms[movingPerm] + directions[movingPerm];
                    iSwap[movingPerm]--;
                    movingPerm = N;
                } else {
                    iSwap[movingPerm] = movingPerm;
                    directions[movingPerm] = -directions[movingPerm];
                    movingPerm--;
                }
            } while (movingPerm > 0);
            return null;
        }
    }

