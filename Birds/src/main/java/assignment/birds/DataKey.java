package assignment.languages;

public class DataKey {
	private String languageName;
	private int languageSize;

	// default constructor
	public DataKey() {
		this(null, 0);
	}
        
	public DataKey(String name, int size) {
		languageName = name;
		languageSize = size;
	}

	public String getLanguageName() {
		return languageName;
	}

	public int getLanguageSize() {
		return languageSize;
	}

	/**
	 * Returns 0 if this DataKey is equal to k, returns -1 if this DataKey is smaller
	 * than k, and it returns 1 otherwise. 
	 */
	public int compareTo(DataKey k) {
            if (this.getLanguageSize() == k.getLanguageSize()) {
                int compare = this.languageName.compareTo(k.getLanguageName());
                if (compare == 0){
                     return 0;
                } 
                else if (compare < 0) {
                    return -1;
                }
            }
            else if(this.getLanguageSize() < k.getLanguageSize()){
                    return -1;
            }
            return 1;
            
	}
}
