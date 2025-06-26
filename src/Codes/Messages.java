public class Messages{
	private LanguageState current,	
			english,
			spanish,
			vietnamese;

	public Messages(){
		english = new EnglishLanguageState();
		spanish = new SpanishLanguageState();
		vietnamese = new VietnameseLanguageState();

		current = english;	
	}

	public void setLanguage(LanguageState language){
		this.current = language;
	}
	
	public LanguageState getEnglishLanguage(){
		return english;
	}

	public LanguageState getSpanishLanguage(){
		return spanish;
	}

	public LanguageState getVietnameseLanguage(){
		return vietnamese;
	}

	public String xPlayerWin(){
		return current.xPlayerWin();
	}

	public String oPlayerWin(){
		return current.oPlayerWin();
	}

	public String tieGame(){
		return current.tieGame();
	}

	public String options(){
		return current.options();
	}

	public String language(){
		return current.language();
	}

	public String help(){
		return current.help();
	}

	public String howToPlay(){
		return current.howToPlay();
	}

	public String moreHelp(){
		return current.moreHelp();
	}

	public String selectMode(){
		return current.selectMode();
	}

	public String vsCPU(){
		return current.vsCPU();
	}

	public String exit(){
		return current.exit();
	}

	public String twoPlayers(){
		return current.twoPlayers();
	}

	public String clickHere(){
		return current.clickHere();
	}

	public String retry(){
		return current.retry();
	}

	public String menu(){
		return current.menu();
	}

	public String instructions(){
		return current.instructions();
	}

	public String moreHelpMessage(){
		return current.moreHelpMessage();
	}
}
