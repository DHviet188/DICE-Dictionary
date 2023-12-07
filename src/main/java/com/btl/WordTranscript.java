package com.btl;

import java.util.ArrayList;

public class WordTranscript {
    
    //private String title = "";
    public String word;
    public ArrayList<phonetic> phonetics; 
    public ArrayList<meaning> meanings;
    
    public class phonetic {
        public String text = "";
        public String audio = "";
    }
    
    public class meaning {
        public String partOfSpeech;
        public ArrayList<definition> definitions;
        public ArrayList<String> synonyms;
        public ArrayList<String> antonyms;
    }
    
    public class definition {
        public String definition;
        public String example;
    }
}
