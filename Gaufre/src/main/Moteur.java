package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Moteur {
	Terrain T;
	int joueur;
	ArrayList<Terrain> histo,redo;
	
	public Moteur(Terrain T){
		this.T=T;
		histo=new ArrayList<Terrain>();
		histo.add(T);
		redo=new ArrayList<Terrain>();
		Random r = new Random();
		joueur = r.nextInt(2) + 1;
	}
	
	void swap_joueur(){
		if(joueur==1) joueur=2;
		else joueur=1;
	}
	
	// Renvoie vrai <=> le coup donné est autorise
	public boolean est_autorise(Point coup){
		if(coup.x<0 || coup.x>=T.l || coup.y<0 || coup.y>=T.h) return false;
		else return T.t[coup.x][coup.y];
	}
	
	// Renvoie une ArrayList des coups autorises (0,0) compris
	public ArrayList<Point> coups_possibles(){
		ArrayList<Point> res=new ArrayList<Point>();
		for(int i=0;i<T.l;i++){
			for(int j=0;j<T.h;j++){
				if(T.t[i][j]) res.add(new Point(i,j));
			}
		}
		return res;
	}
	
	// Renvoie le terrain après le coup donné. Ne modifie pas l'état actuel.
	public Terrain consulter_coup(Point coup){
		Terrain tmp = T.clone();
		int x = coup.x;
		int y = coup.y;
		if(tmp.t[x][y]){
			for(int i=x;i<tmp.l;i++){
				for(int j=y;j<tmp.h;j++){
					tmp.t[i][j]=false;
				}
			}
		}
		return tmp;
	}
	
	// Joue un coup aux coordonnées donnees. Si le coup n'est pas possible, rien ne se passe et retourne 1, 0 sinon.
	public int jouer_coup(Point coup){
		if(est_autorise(coup)){
			T=consulter_coup(coup);
			histo.add(T.clone());
			redo.clear();
			swap_joueur();
			// Maj affichage ?
			return 0;
		}
		else{
			return 1;
		}
	}
	
	// Recule d'un cran dans l'historique. Renvoie 0 si tout s'est bien passé, 1 si on est déjà au terrain de départ.
	public int annuler(){
		if(histo.size()==1) return 1;
		else{
			redo.add(histo.remove(histo.size()-1));
			swap_joueur();
			return 0;
		}
	}
	
	// Le contraire d'annuler. Renvoie 0 si tout s'est bien passé, 1 si on est à la fin de l'historique.
	public int refaire(){
		if(redo.size()==0) return 1;
		else{
			histo.add(redo.remove(redo.size()-1));
			swap_joueur();
			return 0;
		}
	}
}