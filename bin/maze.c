
#define MEM_THRESHOLD (1024 * 1024 * 1024)

#define WALL '#'
#define EMPTY ' '
#define START 'S'
#define END 'E'
#define PATH '@'
#define VISITED '.'

const int NUM_MOVES = 4;
const int I_MOVES[] = {1, -1, 0, 0};
const int J_MOVES[] = {0, 0, 1, -1};

struct IJ {
    const unsigned int i;
    const unsigned int j;
    const IJ *prev;
};

int abs(const int i) {
    const int mask = i >> sizeof(i) - 1;
    return (i ^ mask) - mask;
}

int manhattanDistance(const int i1, const int j1, const int i2, const int j2) {
    return abs(i1 - i2) + abs(j1 - j2);
}

int max(const unsigned int a, const unsigned int b) {
    return a < b ? b : a;
}

int main() {
    const unsigned int n;
    const unsigned int m;
    const unsigned int nm = n * m;
    
    const unsigned int startI;
    const unsigned int startJ;
    
    const unsigned int endI;
    const unsigned int endJ;
    
    int emptyCount = 0;
    
    int numStarts = 0;
    int numEnds = 0;
    
    int tempStartI = -1;
    int tempStartJ = -1;
    
    int tempEndI = -1;
    int tempEndJ = -1;
    
    const char *maze;
    if (nm < MEM_THRESHOLD) {
        maze = malloc(nm);
    } else {
        maze = mmap(0, sizeof *maze, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
    }
    
    char c;
    for (int i = 0; i < m; ++i) {
        for (int j = 0; j < n; ++j) {
            c = maze[i * n + j];
            if (c == START) {
                ++numStarts;
                tempStartI = i;
                tempStartJ = j;
            } else if (c == END) {
                ++numEnds;
                tempEndI = i;
                tempEndJ = j;
            } else if (c == WALL) {
                ++emptyCount;
            }
        }
    }
        
    startI = tempStartI;
    startJ = tempStartJ;
    
    endI = tempEndI;
    endJ = tempEndJ;
    
    maxLength = emptyCount + 2;
    minLength = max(10, Utils.manhattanDistance(tempStartI, tempStartJ, tempEndI, tempEndJ));
}

IJ solve() {
    const unsigned int nextI;
    const unsigned int nextJ;
    const char next;
    const IJ *nextIJ;
    
    for (int move = 0; move < NUM_MOVES; move++) {
        nextI = ij.i + I_MOVES[move];
        nextJ = ij.j = J_MOVES[move];
        next = maze[nextI * n + nextJ];
        nextIJ = malloc(sizeof(IJ));
        
    }
    
    
    for (int move = 0; move < NUM_MOVES; move++) {
            final int nextI = ij.i + I_MOVES[move];
            final int nextJ = ij.j + J_MOVES[move];
            final char next = maze[nextI][nextJ];
            final IJ nextIJ = ij.next(nextI, nextJ);
            if (next == EMPTY) {
                frontier.add(nextIJ, moveNum);
                maze[nextI][nextJ] = VISITED;
            } else if (next == END) {
                return nextIJ;
            }
        }
        return null;
}