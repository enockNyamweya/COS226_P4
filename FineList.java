public class FineList {

    private final Node head;
    private final Node tail;

    public FineList() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);

        head.next = tail;
    }

    public boolean add(int value) {
        this.head.lock.lock(); // lock head and traverse the list to add node
        Node pred = this.head;
        try {
            Node curr = pred.next;
            curr.lock.lock();
            try {
                while (curr.value < value) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }
                if (value == curr.value)
                    return false;

                Node newNode = new Node(value);
                newNode.next = curr;
                pred.next = newNode;
                return true;
            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public boolean remove(int value) {
        this.head.lock.lock();
        Node pred = this.head;
        try {
            Node curr = pred.next;
            curr.lock.lock();

            try {
                while (curr.value < value) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }

                if (curr.value == value) {
                    pred.next = curr.next;
                    return true;
                }

                return false;
            } finally {
                curr.lock.unlock();
            }

        } finally {
            pred.lock.unlock();
        }
    }

    public boolean contains(int value) {
        this.head.lock.lock();
        if (value < this.head.value)
            return false;

        Node pred = this.head;
        try {
            Node curr = pred.next;
            curr.lock.lock();
            // traverse the list and find the node
            try {
                while (curr.value < value) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                    if (curr.value == value)
                        return true;
                }
                return false;

            } finally {
                curr.lock.unlock();
            }

        } finally {
            pred.lock.unlock();
        }

    }
}