package search;

//Let sorted(arr): ∀ i: i + 1 < arr.length && arr[i] >= arr[i + 1]
//Let goodResult(R): arr'[R] <= x && (R - 1 < 0 || arr'[R - 1] > x)
//Let invariant(l, r): r - l >= 1 && (arr[l] > x || l == -1) && (arr[r] <= x || r == arr.length)
//Let immutable(arr): arr'.length == arr.length && ∀ i in [0..arr'.length - 1] arr'[i] == arr[i]
/*Let inside(m, l, r):
            l < r => 2 * l < r + l => l <= (r + l) / 2 => l <= m
            l < r => l + r < 2 * r => (l + r) / 2 < r => m < r
            l <= m < r
*/

public class BinarySearch {

    //Pred: sorted(arr)
    //Post: R: goodResult(R) && immutable(arr)
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] arr = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            arr[i] = Integer.parseInt(args[i + 1]);
        }

        //System.out.println(iterSearch(x, arr));
        System.out.println(recSearch(x, arr, -1, arr.length));
    }

    //Pred: sorted(arr)
    //Post: goodResult(R) && immutable(arr)
    public static int iterSearch(int x, int[] arr) {
        // pred: true
        int l = -1, r = arr.length;
        // post: l == -1, r == list.size()
        // pred: invariant(l, r)
        while (r - l > 1) {
            //pred: invariant(l, r)
            int m = (r + l) / 2;
            //post: invariant(l, r) && m == (r + l) / 2 && inside(m, l, r)
            //pred: invariant(l, r) && m == (r + l) / 2 && inside(m, l, r)
            if (arr[m] > x) {
                //pred: invariant(l, r) && m == (r + l) / 2 && arr[m] > x && inside(m, l, r)
                l = m;
                //post: invariant(l', r) && l' == m && inside(l', l, r)
            } else {
                //pred: invariant(l, r) && m == (r + l) / 2 && arr[m] <= x && inside(m, l, r)
                r = m;
                //post: invariant(l, r') && r' == m && inside(r', l, r)
            }
            //post: invariant(l', r') && (l' == m && r' == r || r' == m && l' == l)
        }
        //post: invariant(l', r')
        //pred: invariant(l, r)
        return r;
        //post: R: goodResult(R)
    }


    //Pred: sorted(arr) && invariant(l, r)
    //Post: R: goodResult(R) && immutable(arr)
    public static int recSearch(int x, int[] arr, int l, int r) {
        //pred: invariant(l, r)
        if (l == r - 1) {
            //pred: l == r - 1 && invariant(l, r)
            return r;
            //post: R: goodResult(R) && immutable(arr)
        }
        //post: invariant(l, r)
        //pred: invariant(l, r)
        int m = (r + l) / 2;
        //post: invariant(l, r) && m == (r + l) / 2 && inside(m, l, r)
        if (arr[m] > x) {
            //pred: invariant(l, r) && m == (r + l) / 2 && arr[m] > x
            return recSearch(x, arr, m, r);
            //post: invariant(l', r) && l' == m && inside(l', l, r)
        }
        //post: invariant(l, r) && m == (r + l) / 2 && arr[m] <= x
        //pred: invariant(l, r) && m == (r + l) / 2 && arr[m] <= x
        return recSearch(x, arr, l, m);
        //post: invariant(l, r') && r' == m && inside(r', l, r)
    }
}
