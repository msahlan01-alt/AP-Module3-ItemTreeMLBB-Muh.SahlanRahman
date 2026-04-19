# 🌟 MLBB Magic Item Tree Lab

## 📸 Screenshots
Program execution results are available in the [`docs/`](docs/) folder.  
Key parts captured include:
- **Tree Lists** – list of items sorted alphabetically, starting from `Magic Shop`.
- **Tree Stats** – total nodes, leaf count, and tree height.
- **Task 2** – most frequent item along with its occurrence count.
- **Task 3** – all crafting paths leading to `Immortality`.
- **Task 4** – interactive item search with `YEAH/NOPE` loop.
- **Task 5** – dynamic item addition, height changes, and removal when max height (6) is reached.

---

## 📋 Observation Questions

### 1. What is the root node in this program?
Root node yang dipakai adalah `Magic Shop`. Dia jadi central point dimana semua basic components pertama kali bisa diakses. Di kode kita, dari `Magic Shop` inilah kita bisa langsung ambil `Mystery Codex`, `Power Crystal`, `Book of Sages`, dan komponen dasar lainnya. Contohnya, kalau kita mau crafting `Lightning Truncheon`, kita harus start dari `Magic Shop`, lalu pilih `Magic Wand` (atau `Power Crystal` atau `Book of Sages`), dan dari salah satu komponen itu kita bisa lanjut ke `Lightning Truncheon`. Jadi root ini berfungsi sebagai gerbang utama ke seluruh isi tree.

### 2. Which nodes are leaf nodes?
Leaf nodes itu simpul yang sudah tidak punya children lagi di dalam tree. Dalam kode kita, leaf bisa berupa item final yang sudah fully crafted, misalnya `Holy Crystal`, `Lightning Truncheon`, `Immortality`, atau `Rose Gold Meteor`. Bisa juga basic components yang kebetulan nggak dipakai sebagai bahan untuk item lain, seperti `Mystery Codex` atau `Power Crystal` kalau mereka berdiri sendiri. Once you reach a leaf, nggak ada lagi percabangan selanjutnya.

### 3. Why is children stored as a `List<ItemNode>` instead of a single variable?
Karena dalam kode kita, sebuah node bisa punya lebih dari satu child. Contohnya `Magic Wand` bisa menjadi bahan untuk `Lightning Truncheon`, `Holy Crystal`, `Genius Wand`, atau `Blood Wings`. Kalau cuma pakai satu variable `ItemNode child`, kita cuma bisa simpan satu kemungkinan next item. Dengan `List<ItemNode>`, kita bisa menyimpan semua cabang yang mungkin sekaligus, jadi struktur tree bisa merepresentasikan banyak jalur crafting yang berbeda, persis kayak di game aslinya.

### 4. What is the difference between a linear structure and a tree structure in this example?
Di program ini, linear structure (misalnya satu `ArrayList`) hanya bisa menampung urutan item dari root ke satu leaf saja, itu pun harus kita tentukan sendiri jalurnya. Sedangkan tree yang kita bangun bisa menyimpan semua kemungkinan jalur crafting dari komponen dasar sampai item final dalam satu struktur data. Setiap node tahu siapa childrennya, sehingga kita bisa menjelajahi semua path yang ada tanpa harus membuat struktur terpisah untuk tiap build. Method `printAllBuildPaths()` yang kita pakai juga bisa langsung ngelist semua kombinasi yang possible.

### 5. How does recursion help when working with trees?  
Rekursi secara natural cocok banget sama struktur tree yang hierarkis. Method seperti `printAllBuildPaths()` atau `findPath()` tinggal call dirinya sendiri untuk setiap child node, jadi kita nggak perlu pusing bikin nested loop yang ribet. Setiap kali recursive call mencapai leaf, dia akan backtrack dengan sendirinya, tinggal remove node terakhir dari path, dan proses eksplorasi cabang lain langsung berjalan mulus. Di kode kita, `printPathsEndingWith()` juga pakai cara yang sama buat filter jalur ke `Immortality`.

### 6. What path is printed when searching for Corrosion Scythe? 
`Corrosion Scythe` sebenarnya bukan bagian dari magic item tree yang kita bangun di program ini. Item ini sengaja dipilih sebagai target pencarian untuk ngetes fitur `findPath()`. Jadi fungsinya lebih ke sample case untuk lihat gimana program ngehandle input item yang not exist di dalam tree. Kalau ngikutin contoh lab asli yang pakai physical attack tree, jalurnya adalah `Start Build → Demon Hunter Sword → Golden Staff → Corrosion Scythe`.

---

## ✅ Take Home Tasks – Implementation Details

### Task 1 – Add a new branch from Start Build using a different first item.
Alih‑alih cuma punya satu parent, Lightning Truncheon sengaja ditempel ke tiga komponen berbeda sekaligus. Ini artinya dari Magic Shop, kita bisa start dengan Magic Wand, Power Crystal, atau Book of Sages, dan ketiganya sama‑sama bisa langsung mengarah ke Lightning Truncheon. Jadi branching nggak cuma terjadi dengan membuat dua node terpisah, tapi juga bisa dengan membuat satu node yang punya multiple parents. Hasilnya, ada beberapa jalur berbeda yang bertemu di item final yang sama.

```java
itemNode lightningTruncheon = new itemNode("Lightning Truncheon", "aoe burst every 6 seconds");

// Lightning Truncheon dijadikan anak dari Magic Wand
magicWand.addChild(lightningTruncheon);
// Juga dijadikan anak dari Power Crystal
powerCrystal.addChild(lightningTruncheon);
// Serta dijadikan anak dari Book of Sages
bookOfSages.addChild(lightningTruncheon);
```

### Task 2 – Create a method named countItemOccurrences()
Method `findMostFrequentItem()` yang ada di kode kita bekerja dengan ngumpulin semua node lewat `collectNodesBFS()`. Setelah semua node kekumpul, kita mapping frekuensi kemunculan tiap nama item ke dalam `HashMap`. Tinggal iterasi sekali lagi buat nyari pasangan nama dan jumlah yang paling tinggi, terus hasilnya langsung di-return sebagai string yang siap tampil. Di output, method ini bakal munculin item apa yang paling sering muncul, misalnya `Holy Crystal (shows up 2 times)` karena kita punya dua node `Holy Crystal`.

```java
// Method untuk mencari item dengan kemunculan terbanyak di dalam tree
static String findMostFrequentItem(itemNode root) {
    // Kumpulkan semua node menggunakan BFS agar aman dari cycle
    List<itemNode> all = new ArrayList<>();
    collectNodesBFS(root, all);
    
    // Hitung frekuensi setiap nama item menggunakan HashMap
    Map<String, Integer> counts = new HashMap<>();
    for (itemNode n : all) {
        // getOrDefault akan mengembalikan 0 jika nama belum ada, lalu ditambah 1
        counts.put(n.name, counts.getOrDefault(n.name, 0) + 1);
    }
    
    // Cari item dengan jumlah kemunculan tertinggi
    String mostFrequent = null;
    int maxCount = 0;
    for (Map.Entry<String, Integer> e : counts.entrySet()) {
        if (e.getValue() > maxCount) {
            maxCount = e.getValue();      // update jumlah terbanyak
            mostFrequent = e.getKey();    // simpan nama item
        }
    }
    // Kembalikan string yang langsung bisa ditampilkan
    return mostFrequent + " (shows up " + maxCount + " times)";
}
```

### Task 3 – Create a method that prints only the paths ending with Immortality
Kita pakai method `printPathsEndingWith()` yang jalan secara rekursif sambil bawa `List<String> path` dan `Set<itemNode> visited`. Setiap masuk node, nama node ditambahin ke path. Kalau node itu leaf dan namanya persis `Immortality`, seluruh jalur langsung kita cetak pake `String.join(" -> ", path)`. Setelah semua anak dijelajahi, node terakhir dihapus dari path. Ini yang bikin proses backtracking berjalan otomatis, hasilnya semua kemungkinan cara crafting `Immortality` langsung muncul semua tanpa kita harus manually track cabang satu-satu.

```java
// Method rekursif untuk mencetak semua jalur crafting yang berakhir dengan item tertentu
static void printPathsEndingWith(itemNode node, String target, List<String> path, Set<itemNode> visited) {
    // Cegah null dan cycle dengan visited set
    if (node == null || visited.contains(node)) return;
    
    visited.add(node);               // tandai node sudah dikunjungi
    path.add(node.name);             // tambahkan node ke jalur saat ini
    
    // Jika node adalah leaf dan namanya sesuai target, cetak jalur
    if (node.children.isEmpty() && node.name.equalsIgnoreCase(target)) {
        System.out.println(String.join(" -> ", path));
    }
    
    // Rekursi ke semua anak
    for (itemNode child : node.children) {
        printPathsEndingWith(child, target, path, visited);
    }
    
    // Backtracking: hapus node setelah selesai menjelajahi cabangnya
    path.remove(path.size() - 1);
}
```

### Task 4 – Modify the program so the user can input the target item name using Scanner
Di bagian ini kita bikin loop sederhana pake `Scanner`. User diminta masukin nama item, dan kalau inputnya kosong loop langsung berhenti. Kalau ada input, kita panggil `findPath()` buat nyari jalur dari root ke item yang dimaksud. Ketemu atau nggak, hasilnya langsung kita tampilin. Setelah itu user ditanya lagi, `search again? (YEAH/NOPE)`, dan loop bakal terus berputar selama jawabannya `YEAH`. Simpel tapi udah cukup interaktif buat eksplorasi jalur secara manual.

```java
// Loop pencarian item dengan opsi mengulang (user input via Scanner)
boolean keepSearching = true;
while (keepSearching) {
    System.out.print("\ntask 4: search item\nenter item name (or just press enter to skip): ");
    String search = scanner.nextLine().trim();
    
    if (search.isEmpty()) {
        keepSearching = false; // user tidak ingin mencari, keluar loop
    } else {
        List<String> path = new ArrayList<>();
        // Cari jalur menuju item yang diminta
        if (findPath(root, search, path, new HashSet<>())) {
            System.out.println("path to " + search + ": " + String.join(" -> ", path));
        } else {
            System.out.println("item not found in the tree. aight.");
        }
        
        // Tanyakan apakah ingin mencari lagi
        System.out.print("search again? (YEAH/NOPE): ");
        String again = scanner.nextLine().trim();
        if (!again.equalsIgnoreCase("YEAH") && !again.equalsIgnoreCase("Y")) {
            keepSearching = false;
        }
    }
}
```

### Task 5 – Add one more level to the tree and observe whether the height changes
Task ini yang paling banyak ngatur logika. Pertama kita cek tinggi tree sekarang pake `height(root)`. Kalau masih di bawah 6, user bisa langsung ketik nama item baru dan item itu bakal ditempel ke leaf paling dalam yang kita dapat dari `findDeepestLeaf()`. Setiap kali nambah, tinggi tree naik satu. Tapi kalau udah mentok di 6, user harus hapus dulu item yang ada dengan `removeNode()` sebelum bisa nambah lagi. Semua perubahan tinggi langsung ditampilin, jadi keliatan jelas efeknya. Batas 6 sendiri ngikutin jumlah slot item di MLBB, jadi ada relevansi langsung sama game-nya.

```java
// Penambahan item baru secara dinamis dengan manajemen tinggi maksimum (6 slot)
boolean adding = true;
while (adding) {
    int currentHeight = height(root);
    System.out.println("current tree height: " + currentHeight + " / 6");
    
    if (currentHeight >= 6) {
        // Tinggi sudah maksimum, harus hapus item dulu sebelum bisa nambah
        System.out.println("tree is already at max height (6). gotta remove an item before adding.");
        System.out.print("enter the name of the item you wanna drop: ");
        String toRemove = scanner.nextLine().trim();
        if (toRemove.isEmpty()) {
            System.out.println("no item removed. aight.");
            adding = false;
        } else {
            if (removeNode(root, toRemove)) {
                System.out.println("item '" + toRemove + "' has been yeeted from the tree.");
                System.out.println("new tree height: " + height(root));
            } else {
                System.out.println("couldn't find that item. try again later.");
            }
        }
    } else {
        // Tinggi belum maksimum, bisa tambah item
        System.out.print("add item? just type the name, or press enter to stop: ");
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty()) {
            adding = false;
            System.out.println("no more items. aight.");
        } else {
            // Cari leaf paling dalam untuk menempelkan item baru agar tinggi bertambah 1
            itemNode parent = findDeepestLeaf(root);
            if (parent != null) {
                parent.addChild(new itemNode(newName, "added item"));
                System.out.println("item '" + newName + "' added under '" + parent.name + "'.");
                System.out.println("new tree height: " + height(root));
            } else {
                System.out.println("couldn't find a leaf to attach to. weird.");
            }
        }
    }
}

```

## 💭 Reflection

Actually, kalau cuma satu build lurus, array sudah cukup. Tapi di MLBB kita sering beradaptasi, jadi tree lebih pas karena dari satu akar we can see all possible routes tanpa perlu daftar tambahan. It's just way more cleaner.

Recursion terasa surprisingly intuitive when i saw `findPath()`, dia bisa mundur sendiri begitu menemui jalan buntu, like it instinctively knows when to backtrack from a dead end without any need for manual stack wrangling. Tantangan terberat adalah menjaga tree agar tidak menjadi cyclic, exactly when users attempt to add or remove items from the tree. Di sinilah BFS (Breadth‑First Search) bekerj, kita periksa pohon tingkat demi tingkat, mulai dari atas lalu turun ke bawah, sambil menandai simpul mana saja yang sudah kita kunjungi. Dengan begitu, program tidak akan terjebak berputar‑putar di tempat yang sama, tidak peduli sebanyak apa items that get thrown in or taken out.
