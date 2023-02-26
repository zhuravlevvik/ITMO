package search;

//Let sorted(arr): ∃ pos : for i : [0: pos - 1] arr[i] > arr[i + 1] && for i : [pos + 1 : arr.length - 1] arr[i] < arr[i + 1]
//Let immutable(arr): arr'.length == arr.length && ∀ i in [0..arr'.length - 1] arr'[i] == arr[i]
//Let invariant(l, r) : r > l && (l == -1 || l + 1 < arr.length && arr[l] >= arr[l + 1]) && (arr[r] < arr[r + 1] || r == arr.length - 1)
//Let goodResult(R): for i in [0..R - 1] arr'[i] >= arr'[i + 1] && (R + 1 == arr'.length || arr'[R] < arr'[R + 1])
/*Let inside(m, l, r):
            l < r => 2 * l < r + l => l <= (r + l) / 2 => l <= m
            l < r => l + r < 2 * r => (l + r) / 2 < r => m < r
            l <= m < r
*/

public class BinarySearchUni {

    //Pred: sorted(arr)
    //Post: R: goodResult(R) && immutable(arr)
    public static void main(String[] args) {
        int[] arr = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            arr[i] = Integer.parseInt(args[i]);
        }

        //System.out.println(iterSearch(arr));
        System.out.println(recSearch(arr, -1, arr.length - 1));
    }

    //Pred: sorted(arr)
    //Post: R: goodResult(R) && immutable(arr)
    public static int iterSearch(int[] arr) {
//        int[] save = Arrays.copyOf(arr, arr.length);
//        Arrays.fill(arr, 0);
        // pred: true
        int l = -1, r = arr.length - 1;
        // post: l == -1, r == list.size() - 1
        // pred: invariant(l, r)
        while (r - l > 1) {
            // pred: invariant(l, r)
            int m = (r + l) / 2;
            //post: invariant(l, r) && m == (r + l) / 2 && inside(m, l, r)
            //pred: invariant(l, r) && m == (r + l) / 2 && inside(m, l, r)
            if (m + 1 == arr.length || arr[m] < arr[m + 1]) {
                //pred: invariant(l, r) && m == (r + l) / 2 && (m + 1 == arr.length || arr[m] < arr[m + 1]) && inside(m, l, r)
                r = m;
                //post: invariant(l, r') && r' == m && inside(r', l, r)
            } else {
                //pred: invariant(l, r) && m == (r + l) / 2 && (m + 1 < arr.length || arr[m] >= arr[m + 1]) && inside(m, l, r)
                l = m;
                //post: invariant(l', r) && l' == m && inside(l', l, r)
            }
            //post: invariant(l', r') && (l' == m && r' == r || r' == m && l' == l)
        }
        //post: invariant(l', r') && immutable(arr) && r' - l' == 1
        //pred: invariant(l, r) && r - l == 1
        return l + 1;
        //post: R: goodResult(R) && immutable(arr)
    }


    //Pred: sorted(arr) && invariant(l, r)
    //Post: R: goodResult(R) && immutable(arr)
    public static int recSearch(int[] arr, int l, int r) {
        //pred: invariant(l, r)
        if (l == r - 1) {
            //pred: r - l == 1 && invariant(l, r)
            return l + 1;
            //R: goodResult(R)
        }
        //post: invariant(l, r)
        //pred: invariant(l, r)
        int m = (r + l) / 2;
        //post: invariant(l, r) && m == (r + l) / 2 && inside(m, l, r)
        //pred: invariant(l, r) && m == (r + l) / 2 && inside(m, l, r)
        if (m + 1 == arr.length || arr[m] < arr[m + 1]) {
            //pred: invariant(l, r) && m == (r + l) / 2 && (m + 1 == arr.length || arr[m] < arr[m + 1])
            return recSearch(arr, l, m);
            //post: invariant(l, r') && r' == m && inside(r', l, r)
        }
        //post: invariant(l, r) && m == (r + l) / 2 && m + 1 < arr.length && arr[m] >= arr[m + 1]
        //pred: invariant(l, r) && m == (r + l) / 2 && m + 1 < arr.length && arr[m] >= arr[m + 1]
        return recSearch(arr, m, r);
        //post: invariant(l', r) && l' == m && inside(l', l, r)
    }
}
