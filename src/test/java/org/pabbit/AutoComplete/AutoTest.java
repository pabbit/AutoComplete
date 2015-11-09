package org.pabbit.AutoComplete;

import java.io.BufferedReader;
import java.io.FileInputStream;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


import edu.emory.mathcs.cs323.trie.autocomplete.*;


public class AutoTest 
{
	private IAutocomplete<?> t_auto;
	
	public AutoTest()
	{
		t_auto = new Autocomplete<>();
	}
	
	public void putDictionary() throws Exception
	{
		String dictFile = "../mycs323/src/dictionary";

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dictFile)));
		String line;
		
		while ((line = reader.readLine()) != null)
			t_auto.put(line.trim(), null);
		
		reader.close();
	}
	
	@Test
	public void autotest() throws Exception
	{
		List<String> cand = new ArrayList<>();
		putDictionary();
		
		cand = t_auto.getCandidates("");
		for(int i = 0; i < cand.size(); i++)
			System.out.print(cand.get(i)+" ");
		
		System.out.println("\n----------------------------------------------------");
		
		t_auto.pickCandidate("jinho", "jingo");
		cand = t_auto.getCandidates("jinho");
		for(int i = 0; i < cand.size(); i++)
			System.out.print(cand.get(i)+" ");

		System.out.println("\n----------------------------------------------------");

//		cand = t_auto.getCandidates("girl");
//		for(int i = 0; i < cand.size(); i++)
//			System.out.print(cand.get(i)+" ");
//		
//		System.out.println("\n----------------------------------------------------");
//		
//		cand = t_auto.getCandidates("girli");
//		for(int i = 0; i < cand.size(); i++)
//			System.out.print(cand.get(i)+" ");
//
//		System.out.println("\n----------------------------------------------------");
//
		t_auto.pickCandidate("girl", "girly");
		cand = t_auto.getCandidates("girl");

		for(int i = 0; i < cand.size(); i++)
			System.out.print(cand.get(i)+ " ");

		System.out.println("\n----------------------------------------------------");

		t_auto.pickCandidate("girl", "girlfriend");
		cand = t_auto.getCandidates("girl");
		
		for(int i = 0; i < cand.size(); i++)
			System.out.print(cand.get(i)+ " ");
		
//		System.out.println("\n----------------------------------------------------");		
//		
//		t_auto.pickCandidate("girl", "girly");
//		cand = t_auto.getCandidates("girl");
//
//		for(int i = 0; i < cand.size(); i++)
//			System.out.print(cand.get(i)+ " ");
	}
}