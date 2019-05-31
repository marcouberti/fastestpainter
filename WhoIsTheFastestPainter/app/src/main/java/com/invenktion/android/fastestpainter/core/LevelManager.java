package com.invenktion.android.fastestpainter.core;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;

import com.invenktion.android.fastestpainter.bean.AmmoBean;
import com.invenktion.android.fastestpainter.bean.PictureBean;
import com.invenktion.android.fastestpainter.bean.SectionArrayList;
import com.invenktion.android.fastestpainter.AtelierChoosingPictureActivity;
import com.invenktion.android.fastestpainter.R;

public class LevelManager {
	private static int currentLevel = 0;
	//Lista di tutte le sezioni
	private static ArrayList<SectionArrayList> sections = new ArrayList<SectionArrayList>();
	//La sezione del gioco corrente, cambier a runtime con una reference ad una delle liste sotto.
	private static SectionArrayList<PictureBean> currentSection;
	private static SectionArrayList<PictureBean> firstSection = new SectionArrayList<PictureBean>();
	private static SectionArrayList<PictureBean> secondSection = new SectionArrayList<PictureBean>();
	private static SectionArrayList<PictureBean> thirdSection = new SectionArrayList<PictureBean>();
	private static SectionArrayList<PictureBean> fourthSection = new SectionArrayList<PictureBean>();
	private static SectionArrayList<PictureBean> fifthSection = new SectionArrayList<PictureBean>();
	//Sezione BONUS:  al di fuori delle modalit di gioco! Ogni quadro  indipendente.
	private static SectionArrayList<PictureBean> bonusSection = new SectionArrayList<PictureBean>();
	//Sezione SOLO in ATELIER:  al di fuori delle modalit di gioco! Si vedono sempre e solo nell'atelier, come quadri diprova.
	private static SectionArrayList<PictureBean> plusAtelierSection = new SectionArrayList<PictureBean>();

	public static void initializeLevels(Context context) {
		currentLevel = -1;
		
		sections.clear();
		sections.add(firstSection);
		sections.add(secondSection);
		sections.add(thirdSection);
		sections.add(fourthSection);
		sections.add(fifthSection);
		sections.add(bonusSection);
		sections.add(plusAtelierSection);
		
		//##########################################################
		//####################  QUADRI DI PROVA ATELIER ############
		//##########################################################
		plusAtelierSection.setSectionName("Atelier");
		plusAtelierSection.setStoryboardImage(R.drawable.sec1storyboard);
		plusAtelierSection.setSfondoImage(R.drawable.sfondo1);
		/*
		plusAtelierSection.setNumber(0);
		plusAtelierSection.setBossResourceNormal(R.drawable.sec1start);
		plusAtelierSection.setBossResourceSuccess(R.drawable.sec1ok);
		plusAtelierSection.setBossResourceFailure(R.drawable.sec1failed);
		plusAtelierSection.setPresentationImage(R.drawable.sec1presentation);
		plusAtelierSection.setLockedImage(R.drawable.sec1locked);
		plusAtelierSection.setTelaImage(R.drawable.tela1);
		plusAtelierSection.setCorniceImage(R.drawable.cornici1);
		*/
		plusAtelierSection.clear();
		plusAtelierSection.add(new PictureBean("mela.png","mela","Apple",R.drawable.mela,R.drawable.mela_tr,new int[] {
				Color.rgb(218, 17, 17),
				Color.rgb(79, 143, 40),
				Color.rgb(129, 67, 12)},
				31000,true,99));
		plusAtelierSection.add(new PictureBean("banane.png","banana","Banana",R.drawable.banane,R.drawable.banane_tr,new int[] {
				Color.rgb(249, 176, 6),
				Color.rgb(120, 138, 18)},
				31000,true,99));
		plusAtelierSection.add(new PictureBean("cocco.png","cocco","Coconut",R.drawable.cocco,R.drawable.cocco_tr,new int[] {
				Color.rgb(175, 110, 16),
				Color.rgb(110, 67, 6)},
				31000,true,99));
		plusAtelierSection.add(new PictureBean("uva.png","uva","Uva",R.drawable.uva,R.drawable.uva_tr,new int[] {
				Color.rgb(170, 14, 176),
				Color.rgb(147, 193, 7),
				Color.rgb(104, 75, 13)},
				31000,true,99));
		plusAtelierSection.add(new PictureBean("lemon.png","lemon","Lemon",R.drawable.lemon,R.drawable.lemon_tr,new int[] {
				Color.rgb(242, 217, 3),
				Color.rgb(248, 240, 176)},
				31000,true,99));
		plusAtelierSection.add(new PictureBean("ciliegie.png","ciliegie","Cherry",R.drawable.ciliegie,R.drawable.ciliegie_tr,new int[] {
				Color.rgb(222, 0, 26)},
				31000,true,99));
		
		/*
		plusAtelierSection.add(new PictureBean("pitturerupestre","Cave painting",R.drawable.pitturerupestre,R.drawable.pitturerupestre_tr,new int[] {
				Color.rgb(207, 107, 58),
				Color.rgb(171, 132, 104)},
				31000,false,91));
		plusAtelierSection.add(new PictureBean("egizi","Egyptian",R.drawable.egizi,R.drawable.egizi_tr,new int[] {
				Color.rgb(155, 96, 26),
				Color.rgb(133, 94, 80),
				Color.rgb(249, 194, 194),
				Color.rgb(228, 148, 113),
				Color.rgb(156, 97, 136),
				Color.rgb(191, 153, 178),
				Color.rgb(155, 155, 155),
				Color.rgb(115, 115, 115)},
				31000,false,91));
				*/
		
		//##########################################################
		//####################  1 SEZIONE ##########################
		//##########################################################
		firstSection.setSectionName("Big Boss");
		firstSection.setNumber(0);
		firstSection.setStoryboardImage(R.drawable.sec1storyboard);
		firstSection.setBossResourceNormal(R.drawable.sec1start);
		firstSection.setBossResourceSuccess(R.drawable.sec1ok);
		firstSection.setBossResourceFailure(R.drawable.sec1failed);
		firstSection.setPresentationImage(R.drawable.sec1presentation);
		firstSection.setLockedImage(R.drawable.sec1locked);
		firstSection.setTelaImage(R.drawable.tela1);
		firstSection.setCorniceImage(R.drawable.cornici1);
		firstSection.setSfondoImage(R.drawable.sfondo1);
		firstSection.clear();
		firstSection.add(new PictureBean("ritratto_federico.png","ritratto_federico","Federico da\nMontefeltro",R.drawable.ritratto_federico,R.drawable.ritratto_federico_tr,new int[] {
				Color.rgb(180, 47, 65),
				Color.rgb(244, 201, 182),
				Color.rgb(183, 178, 175)},
				16000,true,98));
		firstSection.add(new PictureBean("nascitavenere.png","nascitavenere","The Birth of Venus",R.drawable.nascitavenere,R.drawable.nascitavenere_tr,new int[] {
				Color.rgb(229, 174, 86),
				Color.rgb(255, 173, 133),
				Color.rgb(255, 228, 208),
				Color.rgb(216, 236, 244)},
				31000,false,96));
		firstSection.add(new PictureBean("ermellino.png","ermellino","Lady with an Ermine",R.drawable.ermellino,R.drawable.ermellino_tr,new int[] {
				Color.rgb(202, 123, 39),
				Color.rgb(252, 224, 227),
				Color.rgb(85, 127, 98),
				Color.rgb(176, 59, 70),
				Color.rgb(206, 206, 206)},
				21000,false,99));
		firstSection.add(new PictureBean("gioconda.png","gioconda","Mona Lisa",R.drawable.gioconda,R.drawable.gioconda_tr,new int[] {
				Color.rgb(140, 86, 62),
				Color.rgb(244, 205, 210),
				Color.rgb(229, 16, 66),
				Color.rgb(197, 194, 255)},
				21000,false,97));
		firstSection.add(new PictureBean("vitruvian.png","vitruvian","Vitruvian Man",R.drawable.vitruvian,R.drawable.vitruvian_tr,new int[] {
				Color.rgb(87, 73, 57),
				Color.rgb(251, 204, 154),
				Color.rgb(62, 140, 24)},
				21000,false,98));
		
		firstSection.add(new PictureBean("giocatori_carte.png","giocatoricarte","The Card Players",R.drawable.giocatori_carte,R.drawable.giocatori_carte_tr,new int[] {
				Color.rgb(183, 133, 83),
				Color.rgb(201, 187, 90),
				Color.rgb(246, 92, 8),
				Color.rgb(254, 211, 173)},
				31000,false,92));
		firstSection.add(new PictureBean("san_sebastiano.png","sansebastiano","Saint Sebastian",R.drawable.san_sebastiano,R.drawable.san_sebastiano_tr,new int[] {
				Color.rgb(251, 224, 192),
				Color.rgb(239, 237, 222),
				Color.rgb(215, 154, 89)},
				21000,false,79));
		firstSection.add(new PictureBean("youngman.png","youngman","Young Man",R.drawable.youngman,R.drawable.youngman_tr,new int[] {
				Color.rgb(72, 72, 72),
				Color.rgb(214, 162, 107),
				Color.rgb(245, 199, 177)},
				16000,false,98));
		/*
		firstSection.add(new PictureBean("dance","Dancers",R.drawable.dance,R.drawable.dance_tr,new int[] {
				Color.rgb(252, 215, 194),
				Color.rgb(161, 100, 66),
				Color.rgb(130, 196, 81)},
				31000,false,90));
				*/
		firstSection.add(new PictureBean("tre_grazie.png","tregrazie","Three Graces",R.drawable.tre_grazie,R.drawable.tre_grazie_tr,new int[] {
				Color.rgb(255, 225, 225),
				Color.rgb(248, 187, 3),
				Color.rgb(188, 107, 53)},
				21000,false,95));
		firstSection.add(new PictureBean("carlo_7.png","carlosettimo","Charles VII of France",R.drawable.carlo_7,R.drawable.carlo_7_tr,new int[] {
				Color.rgb(144, 62, 48),
				Color.rgb(226, 103, 83),
				Color.rgb(253, 210, 203)},
				16000,false,97));
		firstSection.add(new PictureBean("girasolivangogh.png","girasolivangog","Sunflowers",R.drawable.girasolivangogh,R.drawable.girasolivangogh_tr,new int[] {
				Color.rgb(255, 180, 0),
				Color.rgb(245, 208, 120),
				Color.rgb(123, 174, 43)},
				21000,false,97));
		
		//##########################################################
		//####################  2 SEZIONE ##########################
		//##########################################################
		secondSection.setSectionName("Jean-Luis Baguette");
		secondSection.setNumber(1);
		secondSection.setStoryboardImage(R.drawable.sec2storyboard);
		secondSection.setBossResourceNormal(R.drawable.sec2start);
		secondSection.setBossResourceSuccess(R.drawable.sec2ok);
		secondSection.setBossResourceFailure(R.drawable.sec2failed);
		secondSection.setPresentationImage(R.drawable.sec2presentation);
		secondSection.setLockedImage(R.drawable.sec2locked);
		secondSection.setTelaImage(R.drawable.tela2);
		secondSection.setCorniceImage(R.drawable.cornici2);
		secondSection.setSfondoImage(R.drawable.sfondo2);
		secondSection.clear();
		secondSection.add(new PictureBean("napoleonealpi.png","napoleonealpi","Napoleon\ncrossing the Alps",R.drawable.napoleonealpi,R.drawable.napoleonealpi_tr,new int[] {
				Color.rgb(113, 149, 251),
				Color.rgb(235, 0, 0),
				Color.rgb(247, 223, 196),
				Color.rgb(137, 58, 23),
				Color.rgb(240, 234, 222)},
				31000,true,83));
		secondSection.add(new PictureBean("ficodindaio.png","ficodindaio","Ficodindaio",R.drawable.ficodindaio,R.drawable.ficodindaio_tr,new int[] {
				Color.rgb(255, 139, 46),
				Color.rgb(255, 215, 167),
				Color.rgb(187, 119, 52),
				Color.rgb(223, 244, 249)},
				16000,false,93));
		secondSection.add(new PictureBean("baccogiovane.png","baccogiovane","The adolescent Bacchus",R.drawable.baccogiovane,R.drawable.baccogiovane_tr,new int[] {
				Color.rgb(251, 229, 185),
				Color.rgb(220, 241, 243),
				Color.rgb(172, 195, 126)},
				21000,false,93));
		secondSection.add(new PictureBean("medusa.png","medusa","Medusa",R.drawable.medusa,R.drawable.medusa_tr,new int[] {
				Color.rgb(220, 3, 49),
				Color.rgb(243, 214, 186),
				Color.rgb(130, 181, 89)},
				21000,false,89));
		secondSection.add(new PictureBean("venere_urbino.png","venereurbino","Venus of Urbino",R.drawable.venere_urbino,R.drawable.venere_urbino_tr,new int[] {
				Color.rgb(239, 203, 152),
				Color.rgb(221, 144, 160),
				Color.rgb(224, 244, 246),
				Color.rgb(230, 111, 14)},
				16000,false,92));
		secondSection.add(new PictureBean("ritratto_duchessa_urbino.png","duchessaurbino","Duchess of Urbino",R.drawable.ritratto_duchessa_urbino,R.drawable.ritratto_duchessa_urbino_tr,new int[] {
				Color.rgb(248, 231, 208),
				Color.rgb(154, 151, 148),
				Color.rgb(208, 149, 66)},
				16000,false,95));
		/*
		secondSection.add(new PictureBean("sangerolamo","Saint Jerome",R.drawable.san_gerolamo,R.drawable.san_gerolamo_tr,new int[] {
				Color.rgb(194, 8, 21),
				Color.rgb(202, 127, 29),
				Color.rgb(250, 216, 182)},
				21000,false,91));
				*/
		secondSection.add(new PictureBean("buonaventura.png","buonaventura","The Fortune Teller",R.drawable.buonaventura,R.drawable.buonaventura_tr,new int[] {
				Color.rgb(93, 93, 93),
				Color.rgb(214, 99, 42),
				Color.rgb(250, 216, 182),
				Color.rgb(142, 98, 45)},
				31000,false,92));
		/*
		secondSection.add(new PictureBean("suonatoreliuto","The Lute Player",R.drawable.suonatore_liuto,R.drawable.suonatore_liuto_tr,new int[] {
				Color.rgb(131, 82, 35),
				Color.rgb(254, 222, 193),
				Color.rgb(240, 233, 195),
				Color.rgb(195, 140, 91)},
				31000,false,75));
				*/
		secondSection.add(new PictureBean("morsoramarro.png","morsoramarro","Boy\nbitten by a Lizard",R.drawable.morsoramarro,R.drawable.morsoramarro_tr,new int[] {
				Color.rgb(160, 29, 51),
				Color.rgb(192, 236, 247),
				Color.rgb(121, 168, 100),
				Color.rgb(255, 222, 184),
				Color.rgb(130, 88, 14)},
				21000,false,94));
		secondSection.add(new PictureBean("giardiniera.png","giardiniera","Madonna and Child\nwith Saint John the Baptist",R.drawable.giardiniera,R.drawable.giardiniera_tr,new int[] {
				Color.rgb(255, 222, 207),
				Color.rgb(231, 174, 0),
				Color.rgb(58, 90, 170),
				Color.rgb(212, 10, 10)},
				31000,false,91));
		secondSection.add(new PictureBean("sogno_santelena.png","sognosantelena","Dream of Santa Elena",R.drawable.sogno_santelena,R.drawable.sogno_santelena_tr,new int[] {
				Color.rgb(236, 187, 17),
				Color.rgb(255, 99, 5),
				Color.rgb(240, 205, 177),
				Color.rgb(193,147,81)},
				31000,false,92));
		
		
		//##########################################################
		//####################  3 SEZIONE ##########################
		//##########################################################
		thirdSection.setSectionName("The King");
		thirdSection.setNumber(2);
		thirdSection.setStoryboardImage(R.drawable.sec3storyboard);
		thirdSection.setBossResourceNormal(R.drawable.sec3start);
		thirdSection.setBossResourceSuccess(R.drawable.sec3ok);
		thirdSection.setBossResourceFailure(R.drawable.sec3failed);
		thirdSection.setPresentationImage(R.drawable.sec3presentation);
		thirdSection.setLockedImage(R.drawable.sec3locked);
		thirdSection.setTelaImage(R.drawable.tela3);
		thirdSection.setCorniceImage(R.drawable.cornici3);
		thirdSection.setSfondoImage(R.drawable.sfondo3);
		thirdSection.clear();
		thirdSection.add(new PictureBean("ritratto_van_gogh.png","ritrattovanghog","Van Gogh portrait",R.drawable.ritratto_van_gogh,R.drawable.ritratto_van_gogh_tr,new int[] {
				Color.rgb(116, 150, 188),
				Color.rgb(250, 156, 23),
				Color.rgb(254, 236, 205)},
				21000,false,94));
		thirdSection.add(new PictureBean("annunciazionematthias.png","annunciazionemathias","Annunciation",R.drawable.annunciazionematthias,R.drawable.annunciazionematthias_tr,new int[] {
				Color.rgb(223, 83, 6),
				Color.rgb(235, 226, 178),
				Color.rgb(131, 181, 192),
				Color.rgb(245, 207, 162),
				Color.rgb(168, 95, 42)},
				31000,true,91));
		thirdSection.add(new PictureBean("eleonora_toledo.png","eleonoratoledo","Eleanor of Toledo",R.drawable.eleonora_toledo,R.drawable.eleonora_toledo_tr,new int[] {
				Color.rgb(214, 168, 78),
				Color.rgb(145, 170, 199),
				Color.rgb(251, 230, 212),
				Color.rgb(223, 6, 27),
				Color.rgb(113, 88, 39)},
				31000,false,94));
		thirdSection.add(new PictureBean("john_the_batist.png","giannibattista","John the Baptist",R.drawable.john_the_batist,R.drawable.john_the_batist_tr,new int[] {
				Color.rgb(183, 151, 113),
				Color.rgb(247, 207, 207),
				Color.rgb(188, 107, 53)},
				16000,false,96));
		
		thirdSection.add(new PictureBean("supperemmaus.png","supperemmaus","The supper at Emmaus",R.drawable.supperemmaus,R.drawable.supperemmaus_tr,new int[] {
				Color.rgb(144, 163, 55),
				Color.rgb(189, 16, 20),
				Color.rgb(146, 93, 36),
				Color.rgb(253, 225, 203)},
				31000,false,87));
		thirdSection.add(new PictureBean("autoritrattorembrandth.png","autoritrattorembrant","Rembrandt Portrait",R.drawable.autoritrattorembrandth,R.drawable.autoritrattorembrandth_tr,new int[] {
				Color.rgb(138, 111, 80),
				Color.rgb(208, 148, 77),
				Color.rgb(250, 237, 207),
				Color.rgb(130, 118, 144)},
				21000,false,93));
		
		thirdSection.add(new PictureBean("saint_micheal.png","saintmichael","Saint Michael",R.drawable.saint_micheal,R.drawable.saint_micheal_tr,new int[] {
				Color.rgb(234, 140, 17),
				Color.rgb(245, 192, 192),
				Color.rgb(104, 155, 207),
				Color.rgb(205, 0, 19)},
				31000,false,88));
		thirdSection.add(new PictureBean("vergine_rocce.png","verginerocce","Virgin of the Rocks",R.drawable.vergine_rocce,R.drawable.vergine_rocce_tr,new int[] {
				Color.rgb(133, 190, 239),
				Color.rgb(244, 205, 202),
				Color.rgb(201, 125, 44),
				Color.rgb(187, 198, 208),
				Color.rgb(138, 85, 47)},
				31000,false,90));
		thirdSection.add(new PictureBean("apollo_dafne.png","apollodafne","Apollo and Daphne",R.drawable.apollo_dafne,R.drawable.apollo_dafne_tr,new int[] {
				Color.rgb(142, 184, 87),
				Color.rgb(206, 155, 45),
				Color.rgb(248, 212, 189),
				Color.rgb(177, 89, 41),
				Color.rgb(103, 123, 225)},
				21000,false,92));
		thirdSection.add(new PictureBean("venere_marte.png","venereemarte","Venus and Mars",R.drawable.venere_marte,R.drawable.venere_marte_tr,new int[] {
				Color.rgb(230, 178, 78),
				Color.rgb(240, 196, 181),
				Color.rgb(212, 224, 224)},
				31000,false,88));
		thirdSection.add(new PictureBean("maria_maddalena.png","mariamaddalena","Mary Magdalene",R.drawable.maria_maddalena,R.drawable.maria_maddalena_tr,new int[] {
				Color.rgb(209, 206, 197),
				Color.rgb(214, 0, 0),
				Color.rgb(249, 226, 213)},
				16000,false,95));
		thirdSection.add(new PictureBean("figlia_herodias.png","figliaherodias","Salome\nthe Daughter of Herodias",R.drawable.figlia_herodias,R.drawable.figlia_herodias_tr,new int[] {
				Color.rgb(86, 119, 227),
				Color.rgb(223, 240, 201),
				Color.rgb(240, 205, 177),
				Color.rgb(156, 106, 57),
				Color.rgb(195, 59, 88)},
				21000,false,91));

		//##########################################################
		//####################  4 SEZIONE ##########################
		//##########################################################
		fourthSection.setSectionName("Big Moma");
		fourthSection.setNumber(3);
		fourthSection.setStoryboardImage(R.drawable.sec4storyboard);
		fourthSection.setBossResourceNormal(R.drawable.sec4start);
		fourthSection.setBossResourceSuccess(R.drawable.sec4ok);
		fourthSection.setBossResourceFailure(R.drawable.sec4failed);
		fourthSection.setPresentationImage(R.drawable.sec4presentation);
		fourthSection.setLockedImage(R.drawable.sec4locked);
		fourthSection.setTelaImage(R.drawable.tela4);
		fourthSection.setCorniceImage(R.drawable.cornici4);
		fourthSection.setSfondoImage(R.drawable.sfondo4);
		fourthSection.clear();
		
		fourthSection.add(new PictureBean("ragazzaorecchinoperla.png","ragazzaorecchinoperla","Girl with a Pearl Earring",R.drawable.ragazzaorecchinoperla,R.drawable.ragazzaorecchinoperla_tr,new int[] {
				Color.rgb(245, 176, 55),
				Color.rgb(248, 213, 185),
				Color.rgb(55, 133, 193),
				Color.rgb(245, 55, 91)},
				21000,false,95));
		/*
		fourthSection.add(new PictureBean("mortesanpietro","Death of\nSaint Peter Martyr",R.drawable.morte_sanpietro_martire,R.drawable.morte_sanpietro_martire_tr,new int[] {
				Color.rgb(236, 133, 133),
				Color.rgb(235, 235, 235),
				Color.rgb(246, 202, 202),
				Color.rgb(219, 144, 0),
				Color.rgb(100, 97, 97)},
				31000,true,90));
				*/
		fourthSection.add(new PictureBean("johannfriedrich.png","johnannfriendrick","Johann Friedrich",R.drawable.johannfriedrich,R.drawable.johannfriedrich_tr,new int[] {
				Color.rgb(157, 188, 32),
				Color.rgb(254, 221, 182),
				Color.rgb(187, 106, 62),
				Color.rgb(216, 217, 210)},
				21000,false,86));
		fourthSection.add(new PictureBean("angeliraffaello.png","angeliraffaello","Sistine Madonna angels",R.drawable.angeliraffaello,R.drawable.angeliraffaello_tr,new int[] {
				Color.rgb(254, 228, 195),
				Color.rgb(204, 100, 43),
				Color.rgb(210, 149, 77)},
				21000,false,94));
		fourthSection.add(new PictureBean("bather.png","bather","Bather",R.drawable.bather,R.drawable.bather_tr,new int[] {
				Color.rgb(132, 213, 251),
				Color.rgb(244, 212, 195),
				Color.rgb(212, 134, 93)},
				21000,false,93));
		
		fourthSection.add(new PictureBean("starrynight.png","starrynight","The Starry Night",R.drawable.starrynight,R.drawable.starrynight_tr,new int[] {
				Color.rgb(58, 97, 184),
				Color.rgb(127, 157, 225),
				Color.rgb(241, 189, 31),
				Color.rgb(51, 111, 10)},
				31000,false,97));
		/*
		fourthSection.add(new PictureBean("seedaeroi","Seed Aeroi",R.drawable.seed_areoi,R.drawable.seed_areoi_tr,new int[] {
				Color.rgb(91, 88, 212),
				Color.rgb(178, 121, 90),
				Color.rgb(65, 65, 65)},
				31000,false,94));
				*/
		fourthSection.add(new PictureBean("sleeping_gipsy.png","sleepinggipsy","Sleeping Gipsy",R.drawable.sleeping_gipsy,R.drawable.sleeping_gipsy_tr,new int[] {
				Color.rgb(163, 105, 30),
				Color.rgb(212, 155, 81),
				Color.rgb(213, 241, 242)},
				31000,false,85));
		fourthSection.add(new PictureBean("annazborowska.png","annazborowska","Anna Borowska",R.drawable.annazborowska,R.drawable.annazborowska_tr,new int[] {
				Color.rgb(138, 6, 13),
				Color.rgb(29, 40, 72),
				Color.rgb(255, 219, 219),
				Color.rgb(59, 24, 12)},
				16000,false,98));
		fourthSection.add(new PictureBean("demoiselleavignon.png","demoiselleavignon","Les Demoiselles d'Avignon",R.drawable.demoiselleavignon,R.drawable.demoiselleavignon_tr,new int[] {
				Color.rgb(250, 225, 199),
				Color.rgb(146, 90, 30)},
				21000,false,93));
		fourthSection.add(new PictureBean("juanpareja.png","juanpareja","Juan de Pareja",R.drawable.juanpareja,R.drawable.juanpareja_tr,new int[] {
				Color.rgb(139, 142, 109),
				Color.rgb(233, 233, 233),
				Color.rgb(227, 165, 115),
				Color.rgb(54, 53, 53)},
				16000,false,96));
		fourthSection.add(new PictureBean("souvenirvojage.png","souvenirvojage","Souvenir de vojage",R.drawable.souvenirvojage,R.drawable.souvenirvojage_tr,new int[] {
				Color.rgb(140, 214, 91),
				Color.rgb(93, 162, 96),
				Color.rgb(100, 190, 216)},
				16000,false,100));
		fourthSection.add(new PictureBean("uomobombetta.png","uomobombetta","Man in bowler hat",R.drawable.uomobombetta,R.drawable.uomobombetta_tr,new int[] {
				Color.rgb(91, 91, 91),
				Color.rgb(255, 61, 1),
				Color.rgb(253, 224, 184),
				Color.rgb(47, 221, 7)},
				21000,false,95));
		
		
		//##########################################################
		//####################  5 SEZIONE ##########################
		//##########################################################
		fifthSection.setSectionName("Enigmus");
		fifthSection.setNumber(4);
		fifthSection.setStoryboardImage(R.drawable.sec5storyboard);
		fifthSection.setBossResourceNormal(R.drawable.sec5start);
		fifthSection.setBossResourceSuccess(R.drawable.sec5ok);
		fifthSection.setBossResourceFailure(R.drawable.sec5failed);
		fifthSection.setPresentationImage(R.drawable.sec5presentation);
		fifthSection.setLockedImage(R.drawable.sec5locked);
		fifthSection.setTelaImage(R.drawable.tela5);
		fifthSection.setCorniceImage(R.drawable.cornici5);
		fifthSection.setSfondoImage(R.drawable.sfondo5);
		fifthSection.clear();
		
		fifthSection.add(new PictureBean("urlo.png","urlomunch","The Scream",R.drawable.urlo,R.drawable.urlo_tr,new int[] {
				Color.rgb(203, 192, 158),
				Color.rgb(104, 97, 97),
				Color.rgb(92, 131, 174),
				Color.rgb(251, 122, 71)},
				31000,false,97));
		fifthSection.add(new PictureBean("bernardobrembo.png","bernardobrembo","Man with a Roman Medal",R.drawable.bernardobrembo,R.drawable.bernardobrembo_tr,new int[] {
				Color.rgb(92, 92, 92),
				Color.rgb(211, 186, 111),
				Color.rgb(255, 224, 199)},
				21000,true,96));
		fifthSection.add(new PictureBean("marat.png","marat","The Death of Marat",R.drawable.marat,R.drawable.marat_tr,new int[] {
				Color.rgb(129, 129, 129),
				Color.rgb(193, 152, 89),
				Color.rgb(248, 225, 189)},
				21000,false,92));
		fifthSection.add(new PictureBean("mery_laurent.png","merylaurent","Mery Laurent",R.drawable.mery_laurent,R.drawable.mery_laurent_tr,new int[] {
				Color.rgb(95, 96, 132),
				Color.rgb(255, 238, 245),
				Color.rgb(252, 155, 1),
				Color.rgb(129, 73, 39)},
				21000,false,96));
		fifthSection.add(new PictureBean("giudizio_universale.png","giudiziouniversale","The Creation of Adam",R.drawable.giudizio_universale,R.drawable.giudizio_universale_tr,new int[] {
				Color.rgb(254, 218, 191),
				Color.rgb(195, 122, 68),
				Color.rgb(250, 206, 228),
				Color.rgb(203, 203, 203)},
				31000,false,90));
		fifthSection.add(new PictureBean("frida.png","frida","Frida",R.drawable.frida,R.drawable.frida_tr,new int[] {
				Color.rgb(192, 28, 63),
				Color.rgb(254, 228, 197),
				Color.rgb(82, 82, 82)},
				16000,false,95));
		fifthSection.add(new PictureBean("america_gothic.png","americangothic","American Gothic",R.drawable.america_gothic,R.drawable.america_gothic_tr,new int[] {
				Color.rgb(108, 108, 108),
				Color.rgb(157, 63, 72),
				Color.rgb(235, 245, 251),
				Color.rgb(255, 219, 201),
				Color.rgb(218, 174, 21)},
				21000,false,89));
		fifthSection.add(new PictureBean("watteau.png","watteau","Jean Antoine Watteau",R.drawable.watteau,R.drawable.watteau_tr,new int[] {
				Color.rgb(190, 190, 190),
				Color.rgb(169, 117, 50),
				Color.rgb(255, 238, 224)},
				16000,false,98));
		fifthSection.add(new PictureBean("bacio_klimt.png","bacioklimt","The Kiss",R.drawable.bacio_klimt,R.drawable.bacio_klimt_tr,new int[] {
				Color.rgb(0, 242, 11),
				Color.rgb(255, 222, 0),
				Color.rgb(252, 230, 230),
				Color.rgb(71, 71, 71),
				Color.rgb(238, 118, 0)
				},
				21000,false,90));
		fifthSection.add(new PictureBean("ritratto_monet.png","ritrattomonet","Monet Portrait",R.drawable.ritratto_monet,R.drawable.ritratto_monet_tr,new int[] {
				Color.rgb(41, 63, 132),
				Color.rgb(255, 228, 192),
				Color.rgb(148, 85, 2)},
				16000,false,97));
		fifthSection.add(new PictureBean("tsunami.png","tsunami","Tsunami",R.drawable.tsunami,R.drawable.tsunami_tr,new int[] {
				Color.rgb(112, 144, 235),
				Color.rgb(200, 165, 70)},
				21000,false,80));
		fifthSection.add(new PictureBean("pierrot.png","pierrot","Pierrot",R.drawable.pierrot,R.drawable.pierrot_tr,new int[] {
				Color.rgb(212, 211, 211),
				Color.rgb(242, 187, 163)},
				16000,false,89));
		
		//##########################################################
		//##############  6 SEZIONE BONUS ##########################
		//##########################################################
		bonusSection.setSectionName("Bonus");
		bonusSection.setNumber(5);
		bonusSection.setStoryboardImage(R.drawable.sec1storyboard);
		bonusSection.setBossResourceNormal(R.drawable.jhonnybrushfull);
		bonusSection.setBossResourceSuccess(R.drawable.jhonnybrushfull_8);
		bonusSection.setBossResourceFailure(R.drawable.jhonnybrushfull_9);
		bonusSection.setPresentationImage(R.drawable.bonuspresentation);
		bonusSection.setLockedImage(R.drawable.bonuspresentation);
		bonusSection.setTelaImage(R.drawable.tela6);
		bonusSection.setCorniceImage(R.drawable.cornici1);
		bonusSection.setSfondoImage(R.drawable.sfondo1);
		bonusSection.clear();
		
		bonusSection.add(new PictureBean("zatteramedusa.png","zatteramedusa","The Raft of the Medusa",R.drawable.zatteramedusa,R.drawable.zatteramedusa_tr,new int[] {
				Color.rgb(255, 215, 185),
				Color.rgb(221, 0, 5),
				Color.rgb(191, 108, 27),
				Color.rgb(170, 170, 170),
				Color.rgb(192, 229, 225)},
				61000,true,78));
		bonusSection.add(new PictureBean("libertapopolo.png","libertacheguidapopolo","Liberty Leading the People",R.drawable.libertapopolo,R.drawable.libertapopolo_tr,new int[] {
				Color.rgb(255, 0, 6),
				Color.rgb(40, 112, 211),
				Color.rgb(231, 204, 123),
				Color.rgb(255, 214, 200),
				Color.rgb(164, 92, 66)},
				61000,false,72));
		bonusSection.add(new PictureBean("curiazi.png","curiazi","Oath of the Horatii",R.drawable.curiazi,R.drawable.curiazi_tr,new int[] {
				Color.rgb(230, 4, 4),
				Color.rgb(234, 234, 234),
				Color.rgb(89, 88, 143),
				Color.rgb(251, 227, 214),
				Color.rgb(187, 187, 193)},
				61000,false,85));
		bonusSection.add(new PictureBean("quarto_stato.png","quartostato","The Fourth Estate",R.drawable.quarto_stato,R.drawable.quarto_stato_tr,new int[] {
				Color.rgb(136, 85, 31),
				Color.rgb(207, 155, 100),
				Color.rgb(178, 67, 38),
				Color.rgb(242, 211, 178)},
				61000,false,85));
		bonusSection.add(new PictureBean("persistence_memory.png","persistencememory","Persistence Memory",R.drawable.persistence_memory,R.drawable.persistence_memory_tr,new int[] {
				Color.rgb(200, 190, 152),
				Color.rgb(239, 210, 19),
				Color.rgb(150, 188, 235)},
				61000,false,86));
		
		//La prima sezione  sbloccata di default
		//Il primo livello della prima sezione  SBLOCCATO di default
		((PictureBean)(sections.get(0).get(0))).unlockLevel(context, "arcade");
		sections.get(0).unlockSection(context, "arcade");
		
		//I quadri trial dell'atelier sono sblocati di default
		for(PictureBean bean: plusAtelierSection) {
			bean.unlockLevel(context, "arcade");
		}
		
		//##########################
		//Precarico tutte le bitmap 
		//con softreference, cosi i 
		//men risultano pi fluidi
		//##########################
		int cont = 0;
		for(SectionArrayList<PictureBean> sec:sections) {
			if(cont >= (sections.size() -1)) break;//la sezione trial atelier non la considero
			sec.getPresentaionImage(context);
			sec.getLockedImage(context);
			//precached all levels colored image
			
			//for(PictureBean pic:sec){
				//pic.getColoredPicture(context);
			//}
			cont++;
		}
		//All'inizio
		currentSection = firstSection;
	}

	public static void clearAllCachedImage() {
		for(SectionArrayList<PictureBean> sec:sections) {
			sec.clearSoftReferences();
			for(PictureBean pic:sec){
				pic.clearSoftReferences();
			}
		}
		for(AmmoBean ammo: AmmoManager.getAllAmmo()) {
			ammo.clearSoftReferences();
		}
	}
	
	public static ArrayList<PictureBean> getAllLevels() {
		return currentSection;
	}
	
	public static int getCurrentLevelIndex() {
		return currentLevel;
	}
	
	public static void setCurrentLevelIndex(int currentLevel) {
		if(currentLevel < currentSection.size() && currentLevel >= 0) {
			LevelManager.currentLevel = currentLevel;
		}
	}
	
	public static PictureBean getCurrentLevel() {
		return currentSection.get(currentLevel);
	}
	
	public static PictureBean getNextLevel() {
		if(currentLevel < currentSection.size()-1) {
			return currentSection.get(currentLevel+1);
		}else return null;
	}
	
	public static PictureBean getPreviousLevel() {
		if(currentLevel > 0) {
			currentLevel--;
			return currentSection.get(currentLevel);
		}else return null;
	}
	
	public static Integer[] getAllLevelColorResourceId() {
		int count = currentSection.size();
		Integer[] resourcesId = new Integer[count];
		
		for(int i=0; i<count; i++) {
			resourcesId[i] = currentSection.get(i).getColoredPicture();
		}
		return resourcesId;
	}

	public static int getLevelCount() {
		return currentSection.size();
	}
	
	public static ArrayList<SectionArrayList> getAllSections() {
		return sections;
	}

	public static int getCurrentSectionIndex() {
		return currentSection.getNumber();
	}

	public static int getSectionCount() {
		return sections.size();
	}

	public static SectionArrayList<PictureBean> getBonusSection() {
		return bonusSection;
	}
	
	public static SectionArrayList<PictureBean> getAtelierTrialSection() {
		return plusAtelierSection;
	}
	
	public static void setCurrentSection(int position) {
		/*
		if(position == 0) {
			currentSection = firstSection;
		}else if(position == 1){
			currentSection = secondSection;
		}*/
		if(position >= 0 && position < sections.size()) {
			currentSection = sections.get(position);
		}
		currentLevel = -1;
	}
	
	public static SectionArrayList<PictureBean> getCurrentSection() {
		return currentSection;
	}
	
	//Ritorna la prossima sezione se esiste, altrimenti null
	public static SectionArrayList<PictureBean> getNextSection() {
		int sezioneAttuale = getCurrentSectionIndex();
		if(sezioneAttuale < sections.size()-1) {
			return sections.get(sezioneAttuale + 1);
		}else return null;//non ci sono altre sezioni dopo questa
	}

	public static Bitmap getRandomCorniceBitmap(Context context) {
		Options opts;
		Resources res = context.getResources();
    	opts = new BitmapFactory.Options();
    	opts.inSampleSize=2;
		
		//Scelgo una sezione a caso
		int sectionIndex = (int)(Math.random()*(sections.size()-2)); //-2 perch non voglio far vedere i bonus e atelier
		if(sectionIndex >= sections.size() || sections.get(sectionIndex).size() == 0) {
			sectionIndex = 0;
		}
		SectionArrayList<PictureBean> sceltaSezione = sections.get(sectionIndex);
		//int corniceResource = sceltaSezione.getCorniceImage();
		int corniceResource =R.drawable.cornici1;
		
		int quadroIndex = (int)(Math.random()*sceltaSezione.size());
		if(quadroIndex >= sceltaSezione.size()) {
			quadroIndex = 0;
		}
		PictureBean quadroScelto = sceltaSezione.get(quadroIndex);
		int quadroResource = quadroScelto.getColoredPicture();
		
		
		Paint myPaint = new Paint();
		Bitmap original = BitmapFactory.decodeResource(context.getResources(), corniceResource);  
		Bitmap cornice = Bitmap.createScaledBitmap(original, 200,200, true);
		Bitmap quadro = BitmapFactory.decodeResource(context.getResources(), quadroResource,opts); 
		Bitmap scaledQuadro = Bitmap.createScaledBitmap(quadro, cornice.getWidth()/2,cornice.getWidth()/2, true);
		
		Bitmap finalBitmap = Bitmap.createBitmap(cornice.getWidth(), cornice.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas userResultCanvas = new Canvas(finalBitmap);
        userResultCanvas.drawBitmap(cornice, 0, 0, myPaint);
        userResultCanvas.drawBitmap(scaledQuadro, (cornice.getWidth()/2)-(scaledQuadro.getWidth()/2), (cornice.getWidth()/2)-(scaledQuadro.getWidth()/2), myPaint);
        if(cornice != null && !cornice.isRecycled()) {
        	cornice.recycle();
        	cornice = null;
        }
        if(quadro != null && !quadro.isRecycled()) {
        	quadro.recycle();
        	quadro = null;
        }
        if(scaledQuadro != null && !scaledQuadro.isRecycled()) {
        	scaledQuadro.recycle();
        	scaledQuadro = null;
        }
        if(original != null && !original.isRecycled()) {
        	original.recycle();
        	original = null;
        }
        return finalBitmap;
	}
	
}
