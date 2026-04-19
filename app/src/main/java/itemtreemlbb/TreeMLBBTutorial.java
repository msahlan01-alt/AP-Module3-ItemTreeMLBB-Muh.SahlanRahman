package itemtreemlbb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class TreeMLBBTutorial {
    static class itemNode {
        String name;
        String desc;
        List<itemNode> children;

        itemNode(String name, String desc) {
            this.name = name;
            this.desc = desc;
            this.children = new ArrayList<>();
        }

        public void addChild(itemNode child) {
            children.add(child);
        }
    }

    // BFS to collect all nodes
    static void collectNodesBFS(itemNode root, List<itemNode> result) {
        if (root == null) return;
        Queue<itemNode> queue = new LinkedList<>();
        Set<itemNode> visited = new HashSet<>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            itemNode current = queue.poll();
            result.add(current);
            for (itemNode child : current.children) {
                if (!visited.contains(child)) {
                    visited.add(child);
                    queue.add(child);
                }
            }
        }
    }

    // Print tree: Magic Shop first, then alphabetical
    static void printTreeSorted(itemNode root) {
        List<itemNode> allNodes = new ArrayList<>();
        collectNodesBFS(root, allNodes);
        itemNode magicShop = null;
        List<itemNode> others = new ArrayList<>();
        for (itemNode n : allNodes) {
            if (n.name.equals("Magic Shop")) magicShop = n;
            else others.add(n);
        }
        Collections.sort(others, (a, b) -> a.name.compareToIgnoreCase(b.name));
        if (magicShop != null) {
            System.out.println("-" + magicShop.name + " -> " + magicShop.desc);
        }
        for (itemNode n : others) {
            System.out.println("-" + n.name + " -> " + n.desc);
        }
    }

    static int countNodes(itemNode root) {
        List<itemNode> all = new ArrayList<>();
        collectNodesBFS(root, all);
        return all.size();
    }

    static int countLeaf(itemNode root) {
        List<itemNode> all = new ArrayList<>();
        collectNodesBFS(root, all);
        int leaf = 0;
        for (itemNode n : all) {
            if (n.children.isEmpty()) leaf++;
        }
        return leaf;
    }

    static int height(itemNode node) {
        if (node == null) return 0;
        if (node.children.isEmpty()) return 1;
        int maxChildHeight = 0;
        for (itemNode child : node.children) {
            maxChildHeight = Math.max(maxChildHeight, height(child));
        }
        return maxChildHeight + 1;
    }

    // Find the leaf with the greatest depth (for attaching new items)
    static itemNode findDeepestLeaf(itemNode root) {
        if (root == null) return null;
        Queue<itemNode> queue = new LinkedList<>();
        queue.add(root);
        itemNode deepest = root;
        int maxDepth = 1;
        Map<itemNode, Integer> depthMap = new HashMap<>();
        depthMap.put(root, 1);
        while (!queue.isEmpty()) {
            itemNode cur = queue.poll();
            int curDepth = depthMap.get(cur);
            if (cur.children.isEmpty() && curDepth >= maxDepth) {
                maxDepth = curDepth;
                deepest = cur;
            }
            for (itemNode child : cur.children) {
                depthMap.put(child, curDepth + 1);
                queue.add(child);
            }
        }
        return deepest;
    }

    // Remove a node by name (first occurrence, case‑insensitive)
    static boolean removeNode(itemNode root, String targetName) {
        if (root == null) return false;
        Queue<itemNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            itemNode parent = queue.poll();
            for (Iterator<itemNode> it = parent.children.iterator(); it.hasNext();) {
                itemNode child = it.next();
                if (child.name.equalsIgnoreCase(targetName)) {
                    it.remove();
                    return true;
                }
                queue.add(child);
            }
        }
        return false;
    }

    static boolean findPath(itemNode node, String target, List<String> path, Set<itemNode> visited) {
        if (node == null || visited.contains(node)) return false;
        visited.add(node);
        path.add(node.name);
        if (node.name.equalsIgnoreCase(target)) return true;
        for (itemNode child : node.children) {
            if (findPath(child, target, path, visited)) return true;
        }
        path.remove(path.size() - 1);
        return false;
    }

    static String findMostFrequentItem(itemNode root) {
        List<itemNode> all = new ArrayList<>();
        collectNodesBFS(root, all);
        Map<String, Integer> counts = new HashMap<>();
        for (itemNode n : all) {
            counts.put(n.name, counts.getOrDefault(n.name, 0) + 1);
        }
        String mostFrequent = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            if (e.getValue() > maxCount) {
                maxCount = e.getValue();
                mostFrequent = e.getKey();
            }
        }
        return mostFrequent + " (shows up " + maxCount + " times)";
    }

    static void printPathsEndingWith(itemNode node, String target, List<String> path, Set<itemNode> visited) {
        if (node == null || visited.contains(node)) return;
        visited.add(node);
        path.add(node.name);
        if (node.children.isEmpty() && node.name.equalsIgnoreCase(target)) {
            System.out.println(String.join(" -> ", path));
        }
        for (itemNode child : node.children) {
            printPathsEndingWith(child, target, path, visited);
        }
        path.remove(path.size() - 1);
    }

    static itemNode findNode(itemNode root, String target) {
        List<itemNode> all = new ArrayList<>();
        collectNodesBFS(root, all);
        for (itemNode n : all) {
            if (n.name.equalsIgnoreCase(target)) return n;
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        itemNode root = new itemNode("Magic Shop", "where the real ones start their build");

        // TIER 1 : basic stuff
        itemNode mysteryCodex = new itemNode("Mystery Codex", "cooldown reduction basics");
        itemNode powerCrystal = new itemNode("Power Crystal", "mana basics");
        itemNode magicNecklace = new itemNode("Magic Necklace", "mana regen basics");
        itemNode bookOfSages = new itemNode("Book of Sages", "magic power & cooldown reduction");
        itemNode expertGloves = new itemNode("Expert Gloves", "movement speed basics");
        itemNode flowerOfHope = new itemNode("Flower of Hope", "magic power & health"); // AI Guesses
        itemNode lanternOfHope = new itemNode("Lantern of Hope", "magic power & cooldown"); // AI Guesses
        itemNode magicPotion = new itemNode("Magic Potion", "magic power & mana regen"); // AI Guesses
        itemNode vitalityCrystal = new itemNode("Vitality Crystal", "health points basics");
        itemNode leatherJerkin = new itemNode("Leather Jerkin", "physical defense basics");
        itemNode magicResistCloak = new itemNode("Magic Resist Cloak", "magic defense basics");
        itemNode dagger = new itemNode("Dagger", "physical attack & attack speed basics");

        // defensive component basics
        itemNode aresBelt = new itemNode("Ares Belt", "health & mana defense component");
        itemNode magicKnife = new itemNode("Magic Knife", "hybrid attack & defense knife");
        itemNode regularSpear = new itemNode("Regular Spear", "physical attack & attack speed spear");
        itemNode vampireMallet = new itemNode("Vampire Mallet", "lifesteal & physical attack mallet");

        root.addChild(mysteryCodex);
        root.addChild(powerCrystal);
        root.addChild(magicNecklace);
        root.addChild(bookOfSages);
        root.addChild(expertGloves);
        root.addChild(flowerOfHope);
        root.addChild(lanternOfHope);
        root.addChild(magicPotion);
        root.addChild(vitalityCrystal);
        root.addChild(leatherJerkin);
        root.addChild(magicResistCloak);
        root.addChild(dagger);
        root.addChild(aresBelt);
        root.addChild(magicKnife);
        root.addChild(regularSpear);
        root.addChild(vampireMallet);

        // TIER 2 : the glow up
        itemNode magicWand = new itemNode("Magic Wand", "the core magic power piece");
        mysteryCodex.addChild(magicWand);

        itemNode exoticVeil = new itemNode("Exotic Veil", "magic power & hybrid defense");
        mysteryCodex.addChild(exoticVeil);

        itemNode mysticContainer = new itemNode("Mystic Container", "magic power & lifesteal");
        mysteryCodex.addChild(mysticContainer);

        itemNode tomeOfEvil = new itemNode("Tome of Evil", "magic power & cooldown reduction");
        mysteryCodex.addChild(tomeOfEvil);
        bookOfSages.addChild(tomeOfEvil);

        itemNode azureBlade = new itemNode("Azure Blade", "mana regen & true damage");
        magicNecklace.addChild(azureBlade);

        itemNode elegantGem = new itemNode("Elegant Gem", "health & mana component");
        powerCrystal.addChild(elegantGem);
        vitalityCrystal.addChild(elegantGem);

        itemNode magicBlade = new itemNode("Magic Blade", "physical attack & magic defense");
        dagger.addChild(magicBlade);
        magicResistCloak.addChild(magicBlade);

        itemNode steelLegplates = new itemNode("Steel Legplates", "mid physical defense");
        leatherJerkin.addChild(steelLegplates);

        // TIER 3 : finished items, fully dripped out
        itemNode enchantedTalisman = new itemNode("Enchanted Talisman", "max cooldown & mana regen");
        tomeOfEvil.addChild(enchantedTalisman);
        magicNecklace.addChild(enchantedTalisman);
        magicWand.addChild(enchantedTalisman);

        itemNode lightningTruncheon = new itemNode("Lightning Truncheon", "aoe burst every 6 seconds");
        magicWand.addChild(lightningTruncheon);
        powerCrystal.addChild(lightningTruncheon);
        bookOfSages.addChild(lightningTruncheon);

        itemNode geniusWand = new itemNode("Genius Wand", "lowers enemy magic defense");
        exoticVeil.addChild(geniusWand);
        magicWand.addChild(geniusWand);

        itemNode clockOfDestiny = new itemNode("Clock of Destiny", "scaling magic power & health");
        elegantGem.addChild(clockOfDestiny);
        bookOfSages.addChild(clockOfDestiny);

        itemNode bloodWings = new itemNode("Blood Wings", "shield & movement speed when full health");
        magicWand.addChild(bloodWings);

        itemNode divineGlaive = new itemNode("Divine Glaive", "crazy magic penetration");
        magicWand.addChild(divineGlaive);

        itemNode concentratedEnergy = new itemNode("Concentrated Energy", "magic lifesteal to stay alive");
        magicWand.addChild(concentratedEnergy);
        mysticContainer.addChild(concentratedEnergy);
        vitalityCrystal.addChild(concentratedEnergy);

        itemNode glowingWand = new itemNode("Glowing Wand", "burn damage & anti-shield");
        exoticVeil.addChild(glowingWand);
        magicWand.addChild(glowingWand);

        itemNode holyCrystal1 = new itemNode("Holy Crystal", "massive magic power amplifier");
        magicWand.addChild(holyCrystal1);

        itemNode holyCrystal2 = new itemNode("Holy Crystal", "massive magic power amplifier (alt path)");
        magicWand.addChild(holyCrystal2);

        itemNode featherOfHeaven = new itemNode("Feather of Heaven", "magic damage on basic attack"); // AI Guesses
        exoticVeil.addChild(featherOfHeaven);
        bookOfSages.addChild(featherOfHeaven);

        itemNode starliumScythe = new itemNode("Starlium Scythe", "true damage after a skill"); // AI Guesses
        magicWand.addChild(starliumScythe);
        mysticContainer.addChild(starliumScythe);
        azureBlade.addChild(starliumScythe);

        itemNode iceQueenWand = new itemNode("Ice Queen Wand", "slows on skill damage"); // AI Guesses
        magicWand.addChild(iceQueenWand);
        mysticContainer.addChild(iceQueenWand);
        vitalityCrystal.addChild(iceQueenWand);

        itemNode skyPiercer = new itemNode("Sky Piercer", "executes low health enemies");
        expertGloves.addChild(skyPiercer);

        itemNode fleetingTime = new itemNode("Fleeting Time", "ultimate cooldown on kill/assist"); // AI Guesses
        tomeOfEvil.addChild(fleetingTime);
        expertGloves.addChild(fleetingTime);

        itemNode winterCrown = new itemNode("Winter Crown", "freeze & become invulnerable"); // AI Guesses
        steelLegplates.addChild(winterCrown);
        magicWand.addChild(winterCrown);
        vitalityCrystal.addChild(winterCrown);

        itemNode flaskOfTheOasis = new itemNode("Flask of the Oasis", "shield for yourself or an ally"); // AI Guesses
        mysteryCodex.addChild(flaskOfTheOasis);
        bookOfSages.addChild(flaskOfTheOasis);
        magicWand.addChild(flaskOfTheOasis);

        itemNode wishingLantern = new itemNode("Wishing Lantern", "bonus damage based on enemy health"); // AI Guesses
        tomeOfEvil.addChild(wishingLantern);
        magicWand.addChild(wishingLantern);

        // defensive items with correct recipes
        itemNode immortality = new itemNode("Immortality", "come back to life with a shield");
        aresBelt.addChild(immortality);
        vitalityCrystal.addChild(immortality);
        leatherJerkin.addChild(immortality);

        itemNode roseGold = new itemNode("Rose Gold Meteor", "hybrid lifesteal & shield when low health");
        magicKnife.addChild(roseGold);
        dagger.addChild(roseGold);

        itemNode wind = new itemNode("Wind of Nature", "physical immunity for 2 seconds");
        regularSpear.addChild(wind);
        vampireMallet.addChild(wind);

        // output time
        System.out.println("\ncomprehensive mlbb magic tree (alphabetical order)\n");
        printTreeSorted(root);

        System.out.println("\ntree stats 4 the curious");
        System.out.println("total nodes: " + countNodes(root));
        System.out.println("leaf nodes: " + countLeaf(root));
        System.out.println("tree height: " + height(root));

        System.out.println("\ntask 2: which item is literally everywhere");
        System.out.println("most frequent item: " + findMostFrequentItem(root));

        System.out.println("\ntask 3: all crafting paths to immortality");
        printPathsEndingWith(root, "Immortality", new ArrayList<>(), new HashSet<>());

        // task 4 : search with loop option
        boolean keepSearching = true;
        while (keepSearching) {
            System.out.print("\ntask 4: search item\nenter item name (or just press enter to skip): ");
            String search = scanner.nextLine().trim();
            if (search.isEmpty()) {
                keepSearching = false;
            } else {
                List<String> path = new ArrayList<>();
                if (findPath(root, search, path, new HashSet<>())) {
                    System.out.println("path to " + search + ": " + String.join(" -> ", path));
                } else {
                    System.out.println("item not found in the tree. aight.");
                }
                System.out.print("search again? (YEAH/NOPE): ");
                String again = scanner.nextLine().trim();
                if (!again.equalsIgnoreCase("YEAH") && !again.equalsIgnoreCase("Y")) {
                    keepSearching = false;
                }
            }
        }

        // task 5 : add items with height management (max 6)
        System.out.println("\ntask 5: add new item(s) – max height is 6 (mlbb slots)");
        boolean adding = true;
        while (adding) {
            int currentHeight = height(root);
            System.out.println("current tree height: " + currentHeight + " / 6");
            if (currentHeight >= 6) {
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
                    // after removal, loop again – they can try to add now or press enter to quit
                }
            } else {
                System.out.print("add item? just type the name, or press enter to stop: ");
                String newName = scanner.nextLine().trim();
                if (newName.isEmpty()) {
                    adding = false;
                    System.out.println("no more items. aight.");
                } else {
                    // attach to deepest leaf to increase height by 1
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

        System.out.println("\nfinal tree height: " + height(root));
        scanner.close();
    }
}