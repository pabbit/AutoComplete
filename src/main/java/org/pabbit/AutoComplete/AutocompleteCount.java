package org.pabbit.AutoComplete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import edu.emory.mathcs.cs323.trie.*;
import edu.emory.mathcs.cs323.trie.autocomplete.IAutocomplete;

public class AutocompleteCount<T> extends Trie<T> implements IAutocomplete<T>
{
	final int MAX = 20;
	List<myPair> picks;
	
	public AutocompleteCount()
	{
		super();
		picks = new ArrayList<myPair>();
	}

	@Override
	public List<String> getCandidates(String prefix) 
	{
		List<String> list = new ArrayList<String>();
		
		if(list.size() < MAX)
			list = findCandidates(prefix, list);
				
		return list;
	}
	
	private class nodePair
	{
		private TrieNode<T> node;
		private String prefix;
		
		public nodePair(TrieNode<T> node, String prefix)
		{
			this.node = node;
			this.prefix = prefix;
		}
	}
	
	private void setPrefix(TrieNode<T> node, String prefix)
	{
		char[] array = prefix.toCharArray();
		int i, len = prefix.length();
		node = getRoot();
		
		for (i=0; i<len; i++)
			node = node.addChild(array[i]);
	}
	
	public List<String> findCandidates(String prefix, List<String> list)
	{
		TrieNode<T> node = find(prefix);
		
		Map<Character,TrieNode<T>> map;
		Queue<nodePair> queue = new LinkedList<nodePair>();
		List<Character> keys;
		queue.add(new nodePair(node, prefix));
		nodePair s;
		
		List<String> temp = new ArrayList<>();
		List<String> result = new ArrayList<>();
		
		while(!queue.isEmpty())
		{
			s = queue.poll();
			
			if(s.node.isEndState() && !list.contains(s.prefix))
			{
				if(list.size() >= MAX) break;
				list.add(s.prefix);
			}
			
			map = s.node.getChildrenMap();
			keys = new ArrayList<Character>(map.keySet());
			Collections.sort(keys);
			
			for(Character key : keys)
				queue.add(new nodePair(map.get(key), s.prefix+key));
		}

		if(picks.size() > 0)
		{
			Collections.sort(picks, new CountComparator());

			for(int i = 0; i < picks.size(); i++)
			{
				if(picks.get(i).getPrefix().equals(prefix))
					temp.add(picks.get(i).getCandidate());
			}
			
			for(int j = 0; j < temp.size(); j++)
			{
				for(int n = 0; n < list.size(); n++)
					if(temp.get(j).equals(list.get(n)))
						list.remove(n);
			}
		}
		
		result.addAll(temp);
		result.addAll(list);
		
		if(result.size() >= MAX) result = result.subList(0, MAX);
		
		return result;
	}

	@Override
	public void pickCandidate(String prefix, String candidate) 
	{
		TrieNode<T> node = find(prefix);
		if(node == null) setPrefix(node, prefix);
		if(candidate == "") return;
		String pre = prefix.replaceAll("\\p{Z}", "");
		int count = 1;
		int n = -1;
		node = find(candidate);
		if(node == null) 
		{
			put(candidate, null);
			picks.add(new myPair(pre, candidate, count));
		}
		else
		{
			if(!node.isEndState()) node.setEndState(true);
			
			if(picks.size() > 0)
			{
				for(int j = 0; j <picks.size(); j++)
				{
					if(picks.get(j).getPrefix().equals(pre) && picks.get(j).getCandidate().equals(candidate))
						n = j;
				}
				if(n >= 0)
				{
					int c = picks.get(n).getCount();
					
					picks.get(n).setCount(++c);
				}
				else
					picks.add(new myPair(pre, candidate, count));
			}
			else
				picks.add(0, new myPair(pre, candidate, count));
		}
	}
	
	private class CountComparator implements Comparator<myPair>
	{
		@Override
		public int compare(myPair a, myPair b)
		{
			int c = b.getCount() - a.getCount();
			
			if(c == 0)
				c = a.getCandidate().compareTo(b.getCandidate());
			
			
			return c;
		}
	}
	
	private class myPair
	{
		private String prefix;
		private String candidate;
		private int count;
		
		public myPair(String prefix, String candidate, int count)
		{
			this.prefix = prefix;
			this.candidate = candidate;
			this.count = count;
		}
		
		private void setCount(int count)
		{
			this.count = count;
		}
		
		private String getPrefix()
		{
			return prefix;
		}
		
		private String getCandidate()
		{
			return candidate;
		}
		
		private int getCount()
		{
			return count;
		}
	}

}
