package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */

public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	
	public void build() {
		/** COMPLETE THIS METHOD **/
		if(!(sc.hasNextLine())){
			return;
		}
		root = new TagNode(removeBrackets(sc.nextLine()), null, null);
		Stack<TagNode> stk = new Stack<TagNode>();
		stk.push(root);
		while(sc.hasNext()) {
			String tagOrtext = sc.nextLine();
			//System.out.println(1 + tagOrtext);
			if(tagOrtext.charAt(0) == '<' && tagOrtext.charAt(1) == '/') {
				//System.out.println(tagOrtext + " : closing tag");
				stk.pop();
			}else if(tagOrtext.charAt(0) == '<' && tagOrtext.charAt(1) != '/'){
				TagNode temp = new TagNode(removeBrackets(tagOrtext), null, null);
				//System.out.println(tagOrtext + " : tag");
				if(stk.peek().firstChild == null) {
					stk.peek().firstChild = temp;
				}else {
					TagNode current = stk.peek().firstChild;
					while(current.sibling != null) {
						current = current.sibling;
					}
					current.sibling = temp;
				}
				stk.push(temp);
			}else{
				//System.out.println(tagOrtext);
				TagNode temptext = new TagNode(tagOrtext, null, null);
				if(stk.peek().firstChild == null) {
					stk.peek().firstChild = temptext;
				}else {
					TagNode current = stk.peek().firstChild;
					while(current.sibling != null) {
						current = current.sibling;
					}
					current.sibling = temptext;
				}
			}
		}
	}
	
	private String removeBrackets(String s) {
		return s.substring(1, s.length() - 1);
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		helperReplaceTag(this.root, oldTag, newTag);
		
	}
	
	private void helperReplaceTag(TagNode newRoot, String oldTag, String newTag) {
		if(newRoot == null) {
			return;
		}
		if(newRoot.tag.equals(oldTag)) {
			newRoot.tag = newTag;
		}
		if(newRoot.firstChild != null && newRoot.sibling != null) {
			helperReplaceTag(newRoot.firstChild, oldTag, newTag);
			helperReplaceTag(newRoot.sibling , oldTag, newTag);
		}else if(newRoot.firstChild != null) {
			helperReplaceTag(newRoot.firstChild , oldTag, newTag);
		}else if(newRoot.sibling != null) {
			helperReplaceTag(newRoot.sibling , oldTag, newTag);
		}
	}
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		boldface(row, this.root);
	}
	
	private void boldface(int row, TagNode iteration) {
		if(iteration == null) {
			return;
		}
		
		if(iteration.tag.equals("table")) {
			//System.out.println("Hello sir I am the goat");
			int count = 0;
			TagNode current = iteration.firstChild;
			boolean check = false;
			while(current != null) {
				//System.out.println("I am while loop");
				if(current.tag.equals("tr")) {
					count++;
				}
				if(count == row) {
					check = true;
					break;
				}
				current = current.sibling;
				//System.out.println(current.tag);
			}
			if(check) {
				//System.out.println("Hello sir: " + current.tag);
				TagNode current2 = current.firstChild;
				while(current2 != null) {
					if(current2.tag.equals("td")) {
						TagNode temp = new TagNode("b", current2.firstChild, null);
						current2.firstChild = temp;
					}
					current2 = current2.sibling;
				}
			}else {
				return;
			}
		}
		boldface(row, iteration.firstChild);
		boldface(row, iteration.sibling);
	}
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		if(tag == null || tag.equals("")) {
			return;
		}
		int num = numberOfTags(tag, this.root);
		if(tag.equals("p") || tag.equals("em") || tag.equals("b") || tag.equals("ol") || tag.equals("ul")) {
			for(int i = 0; i < num ; i++) {
				tagRemoval(tag, this.root.firstChild, this.root);
			}
		}
	}
	
	private void tagRemoval(String tag, TagNode target, TagNode parent) {
		if(parent == null || target == null) {
			return;
		}
		if(target.tag.equals(tag)) {
			if(target.tag.equals("ul") || target.tag.equals("ol")) {
				TagNode current = target.firstChild;
				while(current != null) {
					if(current.tag.equals("li")) {
						current.tag = "p";
					}
					current = current.sibling;
				}
			}
			if(target == parent.firstChild) {
				parent.firstChild = target.firstChild;
				TagNode current = target.firstChild;
				while(current.sibling != null) {
					current = current.sibling;
				}
				current.sibling = target.sibling;
			}else if(target == parent.sibling){
				TagNode current = target.firstChild;
				while(current.sibling != null) {
					current = current.sibling;
				}
				current.sibling = target.sibling;
				parent.sibling = target.firstChild;
			}
			return;
		}
		parent = target;
		tagRemoval(tag, target.firstChild, parent);
		tagRemoval(tag, target.sibling, parent);
	}
	
	private int numberOfTags(String tag, TagNode root) {
		if(root == null) {
			return 0;
		}
		if(root.tag.equals(tag)) {
			return 1 + numberOfTags(tag, root.firstChild)+ numberOfTags(tag, root.sibling);
		}else {
			return 0 + numberOfTags(tag, root.firstChild)+ numberOfTags(tag, root.sibling);
		}
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		if(word.equals("") || word == null || tag == null || tag.equals("")) {
			return;
		}
		if(tag.equals("em") || tag.equals("b")) {
			addTagRec(word, tag, this.root);
		}
	}
	
	private void addTagRec(String word, String tag, TagNode root) {
		if(root == null) {
			return;
		}
		TagNode trackRoot = root;
		TagNode trackIt = root.sibling;
		if(root.firstChild != null && root.firstChild.firstChild == null) {
			TagNode parent = root;
			TagNode saveSibling = root.firstChild.sibling;
			root = recAddTag(root.firstChild.tag, tag, word);
			parent.firstChild = root;
			TagNode prev = null;
			TagNode current = root;
			while(current.sibling != null) {
				prev = current;
				current = current.sibling;
			}
			if(prev != null && prev.sibling != null && prev.sibling.tag == null) {
				prev.sibling = saveSibling;
				root = prev;
			}else {
				current.sibling = saveSibling;
				root = current;
			}
			addTagRec(word, tag, parent.sibling);
		}
		if(trackIt != null && trackIt.firstChild == null) {
			TagNode parent = trackRoot;
			TagNode saveSibling = trackIt.sibling;
			trackIt = recAddTag(trackIt.tag, tag, word);
			parent.sibling = trackIt;
			TagNode prev = null;
			TagNode current = trackIt;
			while(current.sibling != null) {
				prev = current;
				current = current.sibling;
			}
			if(prev != null && prev.sibling != null && prev.sibling.tag == null) {
				prev.sibling = saveSibling;
				trackRoot = prev;
			}else {
				current.sibling = saveSibling;
				trackRoot = current;
			}
		}
		addTagRec(word, tag, root.firstChild);
		addTagRec(word, tag, root.sibling);
	}
	
	private boolean checkPunc(char ch){
		return ((ch == '!') || (ch == ':') || (ch == ',') || (ch == '.') || (ch == '?')|| (ch == ';'));
	}
	
	private boolean indexC(String str, String target, int index) {
		if(str.length() == index + target.length() || ((str.length() > index + target.length()) 
				&& (str.charAt(index + target.length()) == ' '))) {
			if(index > 0 && str.charAt(index - 1) == ' ') {
				return true;
			}else if(index == 0) {
				return true;
			}
		}
		if((str.length() >= index + target.length() + 1 && checkPunc(str.charAt(index + target.length())))
				&& ((str.length() >= index + target.length() + 2 && 
				str.charAt(index + target.length() + 1) == ' ') || str.length() == index + target.length() + 1)) {
			if(index > 0 && str.charAt(index - 1) == ' ') {
				return true;
			}else if(index == 0) {
				return true;
			}
		}
		return false;
	}
	
	private TagNode recAddTag(String str, String tag, String target) {
		if(str.equals("") || str == null) {
			return new TagNode(null, null, null);
		}
		int index = str.toLowerCase().indexOf(target.toLowerCase()); 
		boolean check = indexC(str, target, index);
		boolean checkIn = true; 
		while( (index == 0 || (index > 0 && (str.charAt(index - 1) != ' ' || checkIn))) && check == false) {
			index = str.toLowerCase().indexOf(target.toLowerCase(), index + target.length());
			check = indexC(str, target, index);
			if(check == false) {
				checkIn = true;
			}
		}
		if(index == 0) {
			int punI = target.length();
			if(str.length() > punI && (str.charAt(punI) == '.' || str.charAt(punI) == '!' || 
					str.charAt(punI) == ',' || str.charAt(punI) == ':' || str.charAt(punI) == ';' || str.charAt(punI) == '?')) {
				TagNode newTag = new TagNode(tag, null, null);
				newTag.firstChild = new TagNode(str.substring(index, punI + 1), null, null);
				newTag.sibling = recAddTag(str.substring(punI + 1), tag, target); 
				return newTag;
			}else{
				TagNode newTag = new TagNode(tag, null, null);
				newTag.firstChild = new TagNode(str.substring(index, punI), null, null);
				if(punI == str.length()) {
					//System.out.println("==");
					newTag.sibling = recAddTag("", tag, target); 
				}else {
					newTag.sibling = recAddTag(str.substring(punI), tag, target); 
				} 
				return newTag;
			}
			
		}else if(index == -1) {
			TagNode newTag = new TagNode(str, null, null);
			return newTag;
		}else {
			TagNode newTag = new TagNode(str.substring(0, index), null, null);
			newTag.sibling = recAddTag(str.substring(index), tag, target); 
			return newTag;
		}
	}
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
