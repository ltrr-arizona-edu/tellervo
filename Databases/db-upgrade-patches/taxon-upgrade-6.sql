-- Structural changes
ALTER TABLE tlkptaxon ADD COLUMN newlabel VARCHAR;
ALTER TABLE tlkptaxon ADD COLUMN htmllabel VARCHAR;
INSERT INTO tlkptaxonrank (taxonrankid, taxonrank, rankorder) VALUES (19,'subfamily', 60);

-- Higher taxa no longer used
DELETE FROM tlkptaxon where taxonid=807;
DELETE FROM tlkptaxon where taxonid=805;
DELETE FROM tlkptaxon where taxonid=87;
DELETE FROM tlkptaxon where taxonid=259;
DELETE FROM tlkptaxon where taxonid=151;
DELETE FROM tlkptaxon where taxonid=733;
DELETE FROM tlkptaxon where taxonid=266;
DELETE FROM tlkptaxon where taxonid=369;
DELETE FROM tlkptaxon where taxonid=167;
DELETE FROM tlkptaxon where taxonid=129;
DELETE FROM tlkptaxon where taxonid=39;
DELETE FROM tlkptaxon where taxonid=1025;
DELETE FROM tlkptaxon where taxonid=390;
DELETE FROM tlkptaxon where taxonid=2;
DELETE FROM tlkptaxon where taxonid=812;
DELETE FROM tlkptaxon where taxonid=806;
DELETE FROM tlkptaxon where taxonid=603;
DELETE FROM tlkptaxon where taxonid=181;
DELETE FROM tlkptaxon where taxonid=161;
DELETE FROM tlkptaxon where taxonid=533;
DELETE FROM tlkptaxon where taxonid=302;
DELETE FROM tlkptaxon where taxonid=193;
DELETE FROM tlkptaxon where taxonid=661;

-- Higher taxa that are sunk within others
DELETE FROM tlkptaxon where taxonid=49;
DELETE FROM tlkptaxon where taxonid=61;
DELETE FROM tlkptaxon where taxonid=684;
DELETE FROM tlkptaxon where taxonid=177;
DELETE FROM tlkptaxon where taxonid=100;

-- Delete Duplicates
DELETE FROM tlkptaxon WHERE taxonid=352;

-- Clear out old CoL IDs
ALTER TABLE tlkptaxon drop constraint colid;
UPDATE tlkptaxon set colid='';
UPDATE tlkptaxon set colparentid='';


-- Set new CoL IDs 
UPDATE tlkptaxon SET colid='627WF' WHERE taxonid=6;
UPDATE tlkptaxon SET colid='8K9Y' WHERE taxonid=7;
UPDATE tlkptaxon SET colid='63Z5D' WHERE taxonid=8;
UPDATE tlkptaxon SET colid='63Z6Q' WHERE taxonid=9;
UPDATE tlkptaxon SET colid='63Z74' WHERE taxonid=10;
UPDATE tlkptaxon SET colid='63YV6' WHERE taxonid=11;
UPDATE tlkptaxon SET colid='8KB8' WHERE taxonid=12;
UPDATE tlkptaxon SET colid='8KBB' WHERE taxonid=711;
UPDATE tlkptaxon SET colid='63YV8' WHERE taxonid=13;
UPDATE tlkptaxon SET colid='8KBM' WHERE taxonid=14;
UPDATE tlkptaxon SET colid='8KCF' WHERE taxonid=17;
UPDATE tlkptaxon SET colid='5LWNW' WHERE taxonid=18;
UPDATE tlkptaxon SET colid='8KCJ' WHERE taxonid=19;
UPDATE tlkptaxon SET colid='8KCQ' WHERE taxonid=20;
UPDATE tlkptaxon SET colid='8KCY' WHERE taxonid=1015;
UPDATE tlkptaxon SET colid='63YTX' WHERE taxonid=2010;
UPDATE tlkptaxon SET colid='63Z5X' WHERE taxonid=21;
UPDATE tlkptaxon SET colid='8KDR' WHERE taxonid=22;
UPDATE tlkptaxon SET colid='8KDT' WHERE taxonid=23;
UPDATE tlkptaxon SET colid='8KDX' WHERE taxonid=1011;
UPDATE tlkptaxon SET colid='63Z6H' WHERE taxonid=24;
UPDATE tlkptaxon SET colid='63YVK' WHERE taxonid=712;
UPDATE tlkptaxon SET colid='63YVP' WHERE taxonid=27;
UPDATE tlkptaxon SET colid='63YW4' WHERE taxonid=28;
UPDATE tlkptaxon SET colid='63Z83' WHERE taxonid=15;
UPDATE tlkptaxon SET colid='5FBWL' WHERE taxonid=16;
UPDATE tlkptaxon SET colid='8KF3' WHERE taxonid=29;
UPDATE tlkptaxon SET colid='8KFL' WHERE taxonid=30;
UPDATE tlkptaxon SET colid='8KFM' WHERE taxonid=25;
UPDATE tlkptaxon SET colid='5LWQW' WHERE taxonid=26;
UPDATE tlkptaxon SET colid='8KFP' WHERE taxonid=31;
UPDATE tlkptaxon SET colid='8KFR' WHERE taxonid=32;
UPDATE tlkptaxon SET colid='8KFT' WHERE taxonid=33;
UPDATE tlkptaxon SET colid='8KFW' WHERE taxonid=34;
UPDATE tlkptaxon SET colid='7L7J6' WHERE taxonid=35;
UPDATE tlkptaxon SET colid='8KG5' WHERE taxonid=36;
UPDATE tlkptaxon SET colid='63YW5' WHERE taxonid=37;
UPDATE tlkptaxon SET colid='8KGC' WHERE taxonid=38;
UPDATE tlkptaxon SET colid='63Z85' WHERE taxonid=713;
UPDATE tlkptaxon SET colid='LV7' WHERE taxonid=43;
UPDATE tlkptaxon SET colid='8NR9' WHERE taxonid=44;
UPDATE tlkptaxon SET colid='946Z' WHERE taxonid=51;
UPDATE tlkptaxon SET colid='MLD' WHERE taxonid=50;
UPDATE tlkptaxon SET colid='94F5' WHERE taxonid=52;
UPDATE tlkptaxon SET colid='94GR' WHERE taxonid=53;
UPDATE tlkptaxon SET colid='94H3' WHERE taxonid=54;
UPDATE tlkptaxon SET colid='94HF' WHERE taxonid=55;
UPDATE tlkptaxon SET colid='94JD' WHERE taxonid=56;
UPDATE tlkptaxon SET colid='94JH' WHERE taxonid=57;
UPDATE tlkptaxon SET colid='94JK' WHERE taxonid=58;
UPDATE tlkptaxon SET colid='64FL7' WHERE taxonid=59;
UPDATE tlkptaxon SET colid='9X63' WHERE taxonid=63;
UPDATE tlkptaxon SET colid='NYL' WHERE taxonid=62;
UPDATE tlkptaxon SET colid='652F2' WHERE taxonid=67;
UPDATE tlkptaxon SET colid='P7R' WHERE taxonid=66;
UPDATE tlkptaxon SET colid='P8K' WHERE taxonid=68;
UPDATE tlkptaxon SET colid='64QC9' WHERE taxonid=69;
UPDATE tlkptaxon SET colid='64RWW' WHERE taxonid=70;
UPDATE tlkptaxon SET colid='65BKZ' WHERE taxonid=73;
UPDATE tlkptaxon SET colid='PTB' WHERE taxonid=72;
UPDATE tlkptaxon SET colid='Q4Y' WHERE taxonid=163;
UPDATE tlkptaxon SET colid='65FXW' WHERE taxonid=164;
UPDATE tlkptaxon SET colid='Q53' WHERE taxonid=524;
UPDATE tlkptaxon SET colid='65FZQ' WHERE taxonid=525;
UPDATE tlkptaxon SET colid='629CD' WHERE taxonid=74;
UPDATE tlkptaxon SET colid='65JHK' WHERE taxonid=75;
UPDATE tlkptaxon SET colid='65JJG' WHERE taxonid=76;
UPDATE tlkptaxon SET colid='QM5' WHERE taxonid=78;
UPDATE tlkptaxon SET colid='5TQT6' WHERE taxonid=79;
UPDATE tlkptaxon SET colid='662TT' WHERE taxonid=80;
UPDATE tlkptaxon SET colid='65QTX' WHERE taxonid=81;
UPDATE tlkptaxon SET colid='662TY' WHERE taxonid=82;
UPDATE tlkptaxon SET colid='6635Z' WHERE taxonid=83;
UPDATE tlkptaxon SET colid='BCBF' WHERE taxonid=86;
UPDATE tlkptaxon SET colid='R65' WHERE taxonid=85;
UPDATE tlkptaxon SET colid='RJB' WHERE taxonid=1031;
UPDATE tlkptaxon SET colid='BG6W' WHERE taxonid=1079;
UPDATE tlkptaxon SET colid='BG7V' WHERE taxonid=1032;
UPDATE tlkptaxon SET colid='S78' WHERE taxonid=89;
UPDATE tlkptaxon SET colid='BVC9' WHERE taxonid=724;
UPDATE tlkptaxon SET colid='BVD6' WHERE taxonid=90;
UPDATE tlkptaxon SET colid='SQX' WHERE taxonid=93;
UPDATE tlkptaxon SET colid='5FHWF' WHERE taxonid=102;
UPDATE tlkptaxon SET colid='5FHWK' WHERE taxonid=101;
UPDATE tlkptaxon SET colid='C2Q2' WHERE taxonid=94;
UPDATE tlkptaxon SET colid='C2Q9' WHERE taxonid=95;
UPDATE tlkptaxon SET colid='5TWTV' WHERE taxonid=96;
UPDATE tlkptaxon SET colid='5TX67' WHERE taxonid=97;
UPDATE tlkptaxon SET colid='C2SM' WHERE taxonid=98;
UPDATE tlkptaxon SET colid='C2SV' WHERE taxonid=99;
UPDATE tlkptaxon SET colid='C2TP' WHERE taxonid=730;
UPDATE tlkptaxon SET colid='6CK' WHERE taxonid=228;
UPDATE tlkptaxon SET colid='VRN' WHERE taxonid=103;
UPDATE tlkptaxon SET colid='66N99' WHERE taxonid=104;
UPDATE tlkptaxon SET colid='62BGN' WHERE taxonid=107;
UPDATE tlkptaxon SET colid='674CV' WHERE taxonid=108;
UPDATE tlkptaxon SET colid='6FB' WHERE taxonid=106;
UPDATE tlkptaxon SET colid='Q3' WHERE taxonid=749;
UPDATE tlkptaxon SET colid='6LY' WHERE taxonid=312;
UPDATE tlkptaxon SET colid='6MC' WHERE taxonid=750;
UPDATE tlkptaxon SET colid='ZSD' WHERE taxonid=109;
UPDATE tlkptaxon SET colid='67PQJ' WHERE taxonid=110;
UPDATE tlkptaxon SET colid='G67B' WHERE taxonid=111;
UPDATE tlkptaxon SET colid='G67F' WHERE taxonid=714;
UPDATE tlkptaxon SET colid='G67L' WHERE taxonid=112;
UPDATE tlkptaxon SET colid='G67T' WHERE taxonid=113;
UPDATE tlkptaxon SET colid='G67V' WHERE taxonid=114;
UPDATE tlkptaxon SET colid='6MH' WHERE taxonid=77;
UPDATE tlkptaxon SET colid='62CRL' WHERE taxonid=1006;
UPDATE tlkptaxon SET colid='G6P5' WHERE taxonid=1007;
UPDATE tlkptaxon SET colid='62CT9' WHERE taxonid=117;
UPDATE tlkptaxon SET colid='GBMK' WHERE taxonid=118;
UPDATE tlkptaxon SET colid='62CHR' WHERE taxonid=121;
UPDATE tlkptaxon SET colid='GX2B' WHERE taxonid=122;
UPDATE tlkptaxon SET colid='622TP' WHERE taxonid=120;
UPDATE tlkptaxon SET colid='ST' WHERE taxonid=119;
UPDATE tlkptaxon SET colid='352R' WHERE taxonid=124;
UPDATE tlkptaxon SET colid='JF6F' WHERE taxonid=125;
UPDATE tlkptaxon SET colid='JF6K' WHERE taxonid=126;
UPDATE tlkptaxon SET colid='3687' WHERE taxonid=127;
UPDATE tlkptaxon SET colid='JVXR' WHERE taxonid=128;
UPDATE tlkptaxon SET colid='37HJ' WHERE taxonid=715;
UPDATE tlkptaxon SET colid='KFPN' WHERE taxonid=716;
UPDATE tlkptaxon SET colid='38XT' WHERE taxonid=1037;
UPDATE tlkptaxon SET colid='5WFYW' WHERE taxonid=1040;
UPDATE tlkptaxon SET colid='5WFJX' WHERE taxonid=1038;
UPDATE tlkptaxon SET colid='76G' WHERE taxonid=718;
UPDATE tlkptaxon SET colid='39LV' WHERE taxonid=719;
UPDATE tlkptaxon SET colid='LKBJ' WHERE taxonid=720;
UPDATE tlkptaxon SET colid='39SV' WHERE taxonid=131;
UPDATE tlkptaxon SET colid='LMPJ' WHERE taxonid=132;
UPDATE tlkptaxon SET colid='62GLV' WHERE taxonid=133;
UPDATE tlkptaxon SET colid='LP4H' WHERE taxonid=134;
UPDATE tlkptaxon SET colid='LP4J' WHERE taxonid=135;
UPDATE tlkptaxon SET colid='LP7F' WHERE taxonid=136;
UPDATE tlkptaxon SET colid='LP8B' WHERE taxonid=721;
UPDATE tlkptaxon SET colid='LP8N' WHERE taxonid=137;
UPDATE tlkptaxon SET colid='LPBT' WHERE taxonid=140;
UPDATE tlkptaxon SET colid='LPBV' WHERE taxonid=138;
UPDATE tlkptaxon SET colid='LPCK' WHERE taxonid=139;
UPDATE tlkptaxon SET colid='LPCQ' WHERE taxonid=141;
UPDATE tlkptaxon SET colid='5FXVF' WHERE taxonid=145;
UPDATE tlkptaxon SET colid='LPCX' WHERE taxonid=142;
UPDATE tlkptaxon SET colid='LPDF' WHERE taxonid=143;
UPDATE tlkptaxon SET colid='LPG4' WHERE taxonid=144;
UPDATE tlkptaxon SET colid='777' WHERE taxonid=92;
UPDATE tlkptaxon SET colid='77P' WHERE taxonid=178;
UPDATE tlkptaxon SET colid='3BBF' WHERE taxonid=146;
UPDATE tlkptaxon SET colid='MF6V' WHERE taxonid=147;
UPDATE tlkptaxon SET colid='622G7' WHERE taxonid=727;
UPDATE tlkptaxon SET colid='62HQ5' WHERE taxonid=1056;
UPDATE tlkptaxon SET colid='NXGS' WHERE taxonid=1057;
UPDATE tlkptaxon SET colid='62HRS' WHERE taxonid=149;
UPDATE tlkptaxon SET colid='NYJ6' WHERE taxonid=150;
UPDATE tlkptaxon SET colid='7GV' WHERE taxonid=148;
UPDATE tlkptaxon SET colid='7H5' WHERE taxonid=152;
UPDATE tlkptaxon SET colid='62HXK' WHERE taxonid=153;
UPDATE tlkptaxon SET colid='5X65N' WHERE taxonid=154;
UPDATE tlkptaxon SET colid='3FMB' WHERE taxonid=155;
UPDATE tlkptaxon SET colid='68W29' WHERE taxonid=156;
UPDATE tlkptaxon SET colid='697PB' WHERE taxonid=157;
UPDATE tlkptaxon SET colid='68VPF' WHERE taxonid=158;
UPDATE tlkptaxon SET colid='3FS7' WHERE taxonid=159;
UPDATE tlkptaxon SET colid='5WVCR' WHERE taxonid=160;
UPDATE tlkptaxon SET colid='7NV' WHERE taxonid=2011;
UPDATE tlkptaxon SET colid='7PP' WHERE taxonid=377;
UPDATE tlkptaxon SET colid='3HTL' WHERE taxonid=165;
UPDATE tlkptaxon SET colid='RG23' WHERE taxonid=166;
UPDATE tlkptaxon SET colid='3HXZ' WHERE taxonid=169;
UPDATE tlkptaxon SET colid='RHRN' WHERE taxonid=171;
UPDATE tlkptaxon SET colid='RHS3' WHERE taxonid=722;
UPDATE tlkptaxon SET colid='RHS8' WHERE taxonid=170;
UPDATE tlkptaxon SET colid='5XDHD' WHERE taxonid=723;
UPDATE tlkptaxon SET colid='VW' WHERE taxonid=510;
UPDATE tlkptaxon SET colid='3J3X' WHERE taxonid=2004;
UPDATE tlkptaxon SET colid='62KM4' WHERE taxonid=173;
UPDATE tlkptaxon SET colid='RNSP' WHERE taxonid=174;
UPDATE tlkptaxon SET colid='RNSR' WHERE taxonid=175;
UPDATE tlkptaxon SET colid='5XCVW' WHERE taxonid=176;
UPDATE tlkptaxon SET colid='3J7F' WHERE taxonid=2001;
UPDATE tlkptaxon SET colid='7SM' WHERE taxonid=88;
UPDATE tlkptaxon SET colid='62KMN' WHERE taxonid=179;
UPDATE tlkptaxon SET colid='RRZ6' WHERE taxonid=180;
UPDATE tlkptaxon SET colid='62KF2' WHERE taxonid=183;
UPDATE tlkptaxon SET colid='69H3P' WHERE taxonid=184;
UPDATE tlkptaxon SET colid='3JX3' WHERE taxonid=186;
UPDATE tlkptaxon SET colid='RZZT' WHERE taxonid=187;
UPDATE tlkptaxon SET colid='3JXB' WHERE taxonid=188;
UPDATE tlkptaxon SET colid='69GTZ' WHERE taxonid=189;
UPDATE tlkptaxon SET colid='S24D' WHERE taxonid=192;
UPDATE tlkptaxon SET colid='S24H' WHERE taxonid=190;
UPDATE tlkptaxon SET colid='5MXZ7' WHERE taxonid=191;
UPDATE tlkptaxon SET colid='W8' WHERE taxonid=311;
UPDATE tlkptaxon SET colid='3K3G' WHERE taxonid=195;
UPDATE tlkptaxon SET colid='5MY4C' WHERE taxonid=198;
UPDATE tlkptaxon SET colid='S4RG' WHERE taxonid=196;
UPDATE tlkptaxon SET colid='S4SV' WHERE taxonid=197;
UPDATE tlkptaxon SET colid='3KZ2' WHERE taxonid=199;
UPDATE tlkptaxon SET colid='SSK9' WHERE taxonid=200;
UPDATE tlkptaxon SET colid='3KZG' WHERE taxonid=201;
UPDATE tlkptaxon SET colid='SSSJ' WHERE taxonid=204;
UPDATE tlkptaxon SET colid='SSSQ' WHERE taxonid=202;
UPDATE tlkptaxon SET colid='5MZ2G' WHERE taxonid=203;
UPDATE tlkptaxon SET colid='3LZ8' WHERE taxonid=205;
UPDATE tlkptaxon SET colid='TJJ9' WHERE taxonid=206;
UPDATE tlkptaxon SET colid='TJJD' WHERE taxonid=207;
UPDATE tlkptaxon SET colid='TJJH' WHERE taxonid=210;
UPDATE tlkptaxon SET colid='TJJK' WHERE taxonid=211;
UPDATE tlkptaxon SET colid='TJJP' WHERE taxonid=212;
UPDATE tlkptaxon SET colid='5SSS' WHERE taxonid=214;
UPDATE tlkptaxon SET colid='43CGC' WHERE taxonid=215;
UPDATE tlkptaxon SET colid='3Q9V' WHERE taxonid=218;
UPDATE tlkptaxon SET colid='VM58' WHERE taxonid=219;
UPDATE tlkptaxon SET colid='8CK' WHERE taxonid=521;
UPDATE tlkptaxon SET colid='8GM' WHERE taxonid=1058;
UPDATE tlkptaxon SET colid='3SRG' WHERE taxonid=1061;
UPDATE tlkptaxon SET colid='5ZPHM' WHERE taxonid=2005;
UPDATE tlkptaxon SET colid='5GKJ3' WHERE taxonid=2006;
UPDATE tlkptaxon SET colid='5ZRBL' WHERE taxonid=1062;
UPDATE tlkptaxon SET colid='62MK7' WHERE taxonid=728;
UPDATE tlkptaxon SET colid='YB6S' WHERE taxonid=729;
UPDATE tlkptaxon SET colid='6B2HB' WHERE taxonid=1085;
UPDATE tlkptaxon SET colid='8KL' WHERE taxonid=221;
UPDATE tlkptaxon SET colid='ZT' WHERE taxonid=220;
UPDATE tlkptaxon SET colid='62MBV' WHERE taxonid=222;
UPDATE tlkptaxon SET colid='YGJT' WHERE taxonid=223;
UPDATE tlkptaxon SET colid='YGMF' WHERE taxonid=224;
UPDATE tlkptaxon SET colid='62NTW' WHERE taxonid=225;
UPDATE tlkptaxon SET colid='YQ55' WHERE taxonid=226;
UPDATE tlkptaxon SET colid='YQ6W' WHERE taxonid=227;
UPDATE tlkptaxon SET colid='62NTZ' WHERE taxonid=279;
UPDATE tlkptaxon SET colid='6B6VZ' WHERE taxonid=280;
UPDATE tlkptaxon SET colid='62NHK' WHERE taxonid=229;
UPDATE tlkptaxon SET colid='YXRZ' WHERE taxonid=230;
UPDATE tlkptaxon SET colid='3WN6' WHERE taxonid=231;
UPDATE tlkptaxon SET colid='ZY5W' WHERE taxonid=232;
UPDATE tlkptaxon SET colid='8SY' WHERE taxonid=123;
UPDATE tlkptaxon SET colid='3XFC' WHERE taxonid=233;
UPDATE tlkptaxon SET colid='32FVX' WHERE taxonid=234;
UPDATE tlkptaxon SET colid='5NDL4' WHERE taxonid=239;
UPDATE tlkptaxon SET colid='32FWG' WHERE taxonid=235;
UPDATE tlkptaxon SET colid='5NDLK' WHERE taxonid=236;
UPDATE tlkptaxon SET colid='6BRGS' WHERE taxonid=240;
UPDATE tlkptaxon SET colid='32FXZ' WHERE taxonid=241;
UPDATE tlkptaxon SET colid='32FY9' WHERE taxonid=237;
UPDATE tlkptaxon SET colid='5NDMK' WHERE taxonid=238;
UPDATE tlkptaxon SET colid='63259' WHERE taxonid=243;
UPDATE tlkptaxon SET colid='33JMD' WHERE taxonid=244;
UPDATE tlkptaxon SET colid='8WM' WHERE taxonid=242;
UPDATE tlkptaxon SET colid='3Z9D' WHERE taxonid=246;
UPDATE tlkptaxon SET colid='33T93' WHERE taxonid=247;
UPDATE tlkptaxon SET colid='3Z9F' WHERE taxonid=253;
UPDATE tlkptaxon SET colid='33T9W' WHERE taxonid=254;
UPDATE tlkptaxon SET colid='3ZR7' WHERE taxonid=1052;
UPDATE tlkptaxon SET colid='343WR' WHERE taxonid=1053;
UPDATE tlkptaxon SET colid='42PH' WHERE taxonid=1033;
UPDATE tlkptaxon SET colid='34H4K' WHERE taxonid=1034;
UPDATE tlkptaxon SET colid='43TF' WHERE taxonid=1050;
UPDATE tlkptaxon SET colid='6CNPB' WHERE taxonid=1051;
UPDATE tlkptaxon SET colid='44Q7' WHERE taxonid=1086;
UPDATE tlkptaxon SET colid='35PD3' WHERE taxonid=1087;
UPDATE tlkptaxon SET colid='44SG' WHERE taxonid=257;
UPDATE tlkptaxon SET colid='35QMN' WHERE taxonid=258;
UPDATE tlkptaxon SET colid='45ZZ' WHERE taxonid=261;
UPDATE tlkptaxon SET colid='6D8DM' WHERE taxonid=262;
UPDATE tlkptaxon SET colid='35C' WHERE taxonid=376;
UPDATE tlkptaxon SET colid='9CP' WHERE taxonid=653;
UPDATE tlkptaxon SET colid='48BX' WHERE taxonid=264;
UPDATE tlkptaxon SET colid='6DKK8' WHERE taxonid=265;
UPDATE tlkptaxon SET colid='9JQ' WHERE taxonid=260;
UPDATE tlkptaxon SET colid='6332D' WHERE taxonid=731;
UPDATE tlkptaxon SET colid='38YDK' WHERE taxonid=732;
UPDATE tlkptaxon SET colid='4CSV' WHERE taxonid=270;
UPDATE tlkptaxon SET colid='9S8' WHERE taxonid=269;
UPDATE tlkptaxon SET colid='36Z' WHERE taxonid=268;
UPDATE tlkptaxon SET colid='623P8' WHERE taxonid=116;
UPDATE tlkptaxon SET colid='625QY' WHERE taxonid=115;
UPDATE tlkptaxon SET colid='4F2P' WHERE taxonid=273;
UPDATE tlkptaxon SET colid='3BPQQ' WHERE taxonid=274;
UPDATE tlkptaxon SET colid='6GSMS' WHERE taxonid=275;
UPDATE tlkptaxon SET colid='3BPZ6' WHERE taxonid=276;
UPDATE tlkptaxon SET colid='3BQ6Z' WHERE taxonid=277;
UPDATE tlkptaxon SET colid='3BQ7Y' WHERE taxonid=278;
UPDATE tlkptaxon SET colid='6H4NQ' WHERE taxonid=281;
UPDATE tlkptaxon SET colid='3BQJJ' WHERE taxonid=282;
UPDATE tlkptaxon SET colid='4F8D' WHERE taxonid=1097;
UPDATE tlkptaxon SET colid='6GTV8' WHERE taxonid=1098;
UPDATE tlkptaxon SET colid='9Y7' WHERE taxonid=308;
UPDATE tlkptaxon SET colid='623QT' WHERE taxonid=42;
UPDATE tlkptaxon SET colid='383' WHERE taxonid=41;
UPDATE tlkptaxon SET colid='623R7' WHERE taxonid=172;
UPDATE tlkptaxon SET colid='384' WHERE taxonid=91;
UPDATE tlkptaxon SET colid='4J87' WHERE taxonid=283;
UPDATE tlkptaxon SET colid='6HNYV' WHERE taxonid=284;
UPDATE tlkptaxon SET colid='3DSHT' WHERE taxonid=285;
UPDATE tlkptaxon SET colid='3DSJN' WHERE taxonid=286;
UPDATE tlkptaxon SET colid='3DSK5' WHERE taxonid=287;
UPDATE tlkptaxon SET colid='4J8B' WHERE taxonid=1035;
UPDATE tlkptaxon SET colid='3DSL4' WHERE taxonid=1036;
UPDATE tlkptaxon SET colid='4JYX' WHERE taxonid=1026;
UPDATE tlkptaxon SET colid='6HXV3' WHERE taxonid=1027;
UPDATE tlkptaxon SET colid='4K9L' WHERE taxonid=288;
UPDATE tlkptaxon SET colid='6J6YS' WHERE taxonid=289;
UPDATE tlkptaxon SET colid='4KZF' WHERE taxonid=607;
UPDATE tlkptaxon SET colid='6JLHK' WHERE taxonid=608;
UPDATE tlkptaxon SET colid='4L2N' WHERE taxonid=291;
UPDATE tlkptaxon SET colid='6JN3T' WHERE taxonid=292;
UPDATE tlkptaxon SET colid='6JMRD' WHERE taxonid=293;
UPDATE tlkptaxon SET colid='6JMBM' WHERE taxonid=294;
UPDATE tlkptaxon SET colid='6JMHJ' WHERE taxonid=295;
UPDATE tlkptaxon SET colid='6JMHT' WHERE taxonid=296;
UPDATE tlkptaxon SET colid='6JM32' WHERE taxonid=297;
UPDATE tlkptaxon SET colid='4M7G' WHERE taxonid=1075;
UPDATE tlkptaxon SET colid='3F9Y9' WHERE taxonid=1076;
UPDATE tlkptaxon SET colid='4NL8' WHERE taxonid=737;
UPDATE tlkptaxon SET colid='3G3B3' WHERE taxonid=738;
UPDATE tlkptaxon SET colid='623HJ' WHERE taxonid=736;
UPDATE tlkptaxon SET colid='39Q' WHERE taxonid=735;
UPDATE tlkptaxon SET colid='BT' WHERE taxonid=734;
UPDATE tlkptaxon SET colid='4NVR' WHERE taxonid=298;
UPDATE tlkptaxon SET colid='6KH48' WHERE taxonid=299;
UPDATE tlkptaxon SET colid='4PHG' WHERE taxonid=300;
UPDATE tlkptaxon SET colid='3GJJK' WHERE taxonid=301;
UPDATE tlkptaxon SET colid='BZ' WHERE taxonid=267;
UPDATE tlkptaxon SET colid='4QD8' WHERE taxonid=304;
UPDATE tlkptaxon SET colid='3H2VN' WHERE taxonid=305;
UPDATE tlkptaxon SET colid='4RG7' WHERE taxonid=725;
UPDATE tlkptaxon SET colid='3HKMT' WHERE taxonid=726;
UPDATE tlkptaxon SET colid='4SVB' WHERE taxonid=248;
UPDATE tlkptaxon SET colid='6KZVH' WHERE taxonid=249;
UPDATE tlkptaxon SET colid='6KZVB' WHERE taxonid=250;
UPDATE tlkptaxon SET colid='6KZVC' WHERE taxonid=306;
UPDATE tlkptaxon SET colid='624Z2' WHERE taxonid=370;
UPDATE tlkptaxon SET colid='635R7' WHERE taxonid=739;
UPDATE tlkptaxon SET colid='3JH4V' WHERE taxonid=740;
UPDATE tlkptaxon SET colid='4T83' WHERE taxonid=2007;
UPDATE tlkptaxon SET colid='3JJRM' WHERE taxonid=2008;
UPDATE tlkptaxon SET colid='4WWS' WHERE taxonid=309;
UPDATE tlkptaxon SET colid='6LTD3' WHERE taxonid=310;
UPDATE tlkptaxon SET colid='62WJ5' WHERE taxonid=313;
UPDATE tlkptaxon SET colid='6N77K' WHERE taxonid=314;
UPDATE tlkptaxon SET colid='3PF9M' WHERE taxonid=315;
UPDATE tlkptaxon SET colid='6MV99' WHERE taxonid=316;
UPDATE tlkptaxon SET colid='3PFFX' WHERE taxonid=317;
UPDATE tlkptaxon SET colid='3PFRR' WHERE taxonid=318;
UPDATE tlkptaxon SET colid='55DT' WHERE taxonid=1054;
UPDATE tlkptaxon SET colid='3Q7H8' WHERE taxonid=1055;
UPDATE tlkptaxon SET colid='BJB' WHERE taxonid=168;
UPDATE tlkptaxon SET colid='56VV' WHERE taxonid=319;
UPDATE tlkptaxon SET colid='3QRSG' WHERE taxonid=320;
UPDATE tlkptaxon SET colid='6NFM9' WHERE taxonid=321;
UPDATE tlkptaxon SET colid='6NFN8' WHERE taxonid=322;
UPDATE tlkptaxon SET colid='56YL' WHERE taxonid=323;
UPDATE tlkptaxon SET colid='6NGH4' WHERE taxonid=324;
UPDATE tlkptaxon SET colid='6NGGD' WHERE taxonid=327;
UPDATE tlkptaxon SET colid='6NGFW' WHERE taxonid=328;
UPDATE tlkptaxon SET colid='6NGHR' WHERE taxonid=330;
UPDATE tlkptaxon SET colid='5J6LL' WHERE taxonid=331;
UPDATE tlkptaxon SET colid='6NGHP' WHERE taxonid=329;
UPDATE tlkptaxon SET colid='6NGHZ' WHERE taxonid=620;
UPDATE tlkptaxon SET colid='5PKXC' WHERE taxonid=621;
UPDATE tlkptaxon SET colid='6NFZD' WHERE taxonid=332;
UPDATE tlkptaxon SET colid='6NFYT' WHERE taxonid=1017;
UPDATE tlkptaxon SET colid='6NFYV' WHERE taxonid=333;
UPDATE tlkptaxon SET colid='6NFYP' WHERE taxonid=334;
UPDATE tlkptaxon SET colid='6NFYQ' WHERE taxonid=811;
UPDATE tlkptaxon SET colid='6NFYL' WHERE taxonid=335;
UPDATE tlkptaxon SET colid='6NFYK' WHERE taxonid=753;
UPDATE tlkptaxon SET colid='5PKYG' WHERE taxonid=754;
UPDATE tlkptaxon SET colid='6NFZX' WHERE taxonid=336;
UPDATE tlkptaxon SET colid='6NFZW' WHERE taxonid=337;
UPDATE tlkptaxon SET colid='6NFZQ' WHERE taxonid=342;
UPDATE tlkptaxon SET colid='6NFZN' WHERE taxonid=338;
UPDATE tlkptaxon SET colid='5PKYQ' WHERE taxonid=617;
UPDATE tlkptaxon SET colid='6NGBR' WHERE taxonid=325;
UPDATE tlkptaxon SET colid='5J6M3' WHERE taxonid=326;
UPDATE tlkptaxon SET colid='6NGBG' WHERE taxonid=618;
UPDATE tlkptaxon SET colid='6NGBF' WHERE taxonid=339;
UPDATE tlkptaxon SET colid='6NGBB' WHERE taxonid=340;
UPDATE tlkptaxon SET colid='6NHWF' WHERE taxonid=341;
UPDATE tlkptaxon SET colid='6NHVZ' WHERE taxonid=619;
UPDATE tlkptaxon SET colid='6NHW6' WHERE taxonid=343;
UPDATE tlkptaxon SET colid='5864' WHERE taxonid=1099;
UPDATE tlkptaxon SET colid='3R6XS' WHERE taxonid=2000;
UPDATE tlkptaxon SET colid='59QH' WHERE taxonid=348;
UPDATE tlkptaxon SET colid='3RL8J' WHERE taxonid=349;
UPDATE tlkptaxon SET colid='62VP6' WHERE taxonid=255;
UPDATE tlkptaxon SET colid='6NT8V' WHERE taxonid=256;
UPDATE tlkptaxon SET colid='3F4' WHERE taxonid=216;
UPDATE tlkptaxon SET colid='62XM8' WHERE taxonid=1041;
UPDATE tlkptaxon SET colid='6NWM4' WHERE taxonid=1042;
UPDATE tlkptaxon SET colid='3S7H4' WHERE taxonid=1088;
UPDATE tlkptaxon SET colid='639D7' WHERE taxonid=350;
UPDATE tlkptaxon SET colid='6NYWF' WHERE taxonid=353;
UPDATE tlkptaxon SET colid='6NZ8C' WHERE taxonid=351;
UPDATE tlkptaxon SET colid='5PNDP' WHERE taxonid=1001;
UPDATE tlkptaxon SET colid='6NZ8D' WHERE taxonid=354;
UPDATE tlkptaxon SET colid='5PNDX' WHERE taxonid=355;
UPDATE tlkptaxon SET colid='6NYVX' WHERE taxonid=356;
UPDATE tlkptaxon SET colid='6NYW8' WHERE taxonid=357;
UPDATE tlkptaxon SET colid='6NYY5' WHERE taxonid=358;
UPDATE tlkptaxon SET colid='6NYY7' WHERE taxonid=741;
UPDATE tlkptaxon SET colid='6NZ9P' WHERE taxonid=359;
UPDATE tlkptaxon SET colid='BTB' WHERE taxonid=425;
UPDATE tlkptaxon SET colid='3F8' WHERE taxonid=424;
UPDATE tlkptaxon SET colid='624WH' WHERE taxonid=130;
UPDATE tlkptaxon SET colid='5BR4' WHERE taxonid=360;
UPDATE tlkptaxon SET colid='3SW5J' WHERE taxonid=361;
UPDATE tlkptaxon SET colid='5CLL' WHERE taxonid=362;
UPDATE tlkptaxon SET colid='3TDSS' WHERE taxonid=363;
UPDATE tlkptaxon SET colid='5CMX' WHERE taxonid=1093;
UPDATE tlkptaxon SET colid='3TFD5' WHERE taxonid=1094;
UPDATE tlkptaxon SET colid='5DQJ' WHERE taxonid=801;
UPDATE tlkptaxon SET colid='724CH' WHERE taxonid=802;
UPDATE tlkptaxon SET colid='62WRV' WHERE taxonid=364;
UPDATE tlkptaxon SET colid='6PWDZ' WHERE taxonid=365;
UPDATE tlkptaxon SET colid='6PWDV' WHERE taxonid=366;
UPDATE tlkptaxon SET colid='62X65' WHERE taxonid=367;
UPDATE tlkptaxon SET colid='6PYTF' WHERE taxonid=368;
UPDATE tlkptaxon SET colid='5FBY' WHERE taxonid=371;
UPDATE tlkptaxon SET colid='6QJRL' WHERE taxonid=372;
UPDATE tlkptaxon SET colid='5FCK' WHERE taxonid=374;
UPDATE tlkptaxon SET colid='3VCGS' WHERE taxonid=375;
UPDATE tlkptaxon SET colid='5GFJ' WHERE taxonid=378;
UPDATE tlkptaxon SET colid='3VZJR' WHERE taxonid=379;
UPDATE tlkptaxon SET colid='5K5W' WHERE taxonid=380;
UPDATE tlkptaxon SET colid='3XH5C' WHERE taxonid=742;
UPDATE tlkptaxon SET colid='3XH7J' WHERE taxonid=385;
UPDATE tlkptaxon SET colid='3XHB5' WHERE taxonid=381;
UPDATE tlkptaxon SET colid='3XHF6' WHERE taxonid=386;
UPDATE tlkptaxon SET colid='3XHLF' WHERE taxonid=382;
UPDATE tlkptaxon SET colid='CC9' WHERE taxonid=373;
UPDATE tlkptaxon SET colid='3HL' WHERE taxonid=105;
UPDATE tlkptaxon SET colid='MG' WHERE taxonid=40;
UPDATE tlkptaxon SET colid='MLP' WHERE taxonid=307;
UPDATE tlkptaxon SET colid='CDB' WHERE taxonid=1044;
UPDATE tlkptaxon SET colid='CDB' WHERE taxonid=61;
UPDATE tlkptaxon SET colid='3HP' WHERE taxonid=60;
UPDATE tlkptaxon SET colid='5KWC' WHERE taxonid=383;
UPDATE tlkptaxon SET colid='6R6H5' WHERE taxonid=384;
UPDATE tlkptaxon SET colid='5KXJ' WHERE taxonid=251;
UPDATE tlkptaxon SET colid='3XVQM' WHERE taxonid=252;
UPDATE tlkptaxon SET colid='CKB' WHERE taxonid=185;
UPDATE tlkptaxon SET colid='5T9G' WHERE taxonid=1070;
UPDATE tlkptaxon SET colid='43HYQ' WHERE taxonid=1071;
UPDATE tlkptaxon SET colid='63BT8' WHERE taxonid=1063;
UPDATE tlkptaxon SET colid='43QM6' WHERE taxonid=1064;
UPDATE tlkptaxon SET colid='CXM' WHERE taxonid=213;
UPDATE tlkptaxon SET colid='5V62' WHERE taxonid=392;
UPDATE tlkptaxon SET colid='44CWQ' WHERE taxonid=393;
UPDATE tlkptaxon SET colid='5VCQ' WHERE taxonid=387;
UPDATE tlkptaxon SET colid='44FPX' WHERE taxonid=388;
UPDATE tlkptaxon SET colid='73R6J' WHERE taxonid=389;
UPDATE tlkptaxon SET colid='5WHT' WHERE taxonid=394;
UPDATE tlkptaxon SET colid='458DN' WHERE taxonid=395;
UPDATE tlkptaxon SET colid='D3J' WHERE taxonid=391;
UPDATE tlkptaxon SET colid='627BN' WHERE taxonid=604;
UPDATE tlkptaxon SET colid='5WR8' WHERE taxonid=605;
UPDATE tlkptaxon SET colid='73SY5' WHERE taxonid=606;
UPDATE tlkptaxon SET colid='D48' WHERE taxonid=272;
UPDATE tlkptaxon SET colid='3LY' WHERE taxonid=271;
UPDATE tlkptaxon SET colid='63F3M' WHERE taxonid=1077;
UPDATE tlkptaxon SET colid='47BYZ' WHERE taxonid=1078;
UPDATE tlkptaxon SET colid='626G3' WHERE taxonid=396;
UPDATE tlkptaxon SET colid='654M' WHERE taxonid=397;
UPDATE tlkptaxon SET colid='47SYX' WHERE taxonid=398;
UPDATE tlkptaxon SET colid='47SYY' WHERE taxonid=399;
UPDATE tlkptaxon SET colid='47SZ5' WHERE taxonid=400;
UPDATE tlkptaxon SET colid='6SB9B' WHERE taxonid=401;
UPDATE tlkptaxon SET colid='47SZJ' WHERE taxonid=402;
UPDATE tlkptaxon SET colid='47SZR' WHERE taxonid=403;
UPDATE tlkptaxon SET colid='74B9B' WHERE taxonid=404;
UPDATE tlkptaxon SET colid='47SZY' WHERE taxonid=405;
UPDATE tlkptaxon SET colid='47T24' WHERE taxonid=406;
UPDATE tlkptaxon SET colid='47T26' WHERE taxonid=407;
UPDATE tlkptaxon SET colid='47T2C' WHERE taxonid=408;
UPDATE tlkptaxon SET colid='47T2H' WHERE taxonid=409;
UPDATE tlkptaxon SET colid='DG4' WHERE taxonid=511;
UPDATE tlkptaxon SET colid='665H' WHERE taxonid=411;
UPDATE tlkptaxon SET colid='487MG' WHERE taxonid=743;
UPDATE tlkptaxon SET colid='487MQ' WHERE taxonid=412;
UPDATE tlkptaxon SET colid='DGG' WHERE taxonid=410;
UPDATE tlkptaxon SET colid='DK4' WHERE taxonid=290;
UPDATE tlkptaxon SET colid='63H3Q' WHERE taxonid=413;
UPDATE tlkptaxon SET colid='6TCXD' WHERE taxonid=414;
UPDATE tlkptaxon SET colid='75D9H' WHERE taxonid=1013;
UPDATE tlkptaxon SET colid='63HRW' WHERE taxonid=415;
UPDATE tlkptaxon SET colid='75FSZ' WHERE taxonid=416;
UPDATE tlkptaxon SET colid='63LR7' WHERE taxonid=417;
UPDATE tlkptaxon SET colid='4D8TK' WHERE taxonid=418;
UPDATE tlkptaxon SET colid='6HWN' WHERE taxonid=419;
UPDATE tlkptaxon SET colid='4DN4H' WHERE taxonid=420;
UPDATE tlkptaxon SET colid='6JPP' WHERE taxonid=422;
UPDATE tlkptaxon SET colid='6TVD7' WHERE taxonid=423;
UPDATE tlkptaxon SET colid='6L84' WHERE taxonid=1047;
UPDATE tlkptaxon SET colid='76SMX' WHERE taxonid=1048;
UPDATE tlkptaxon SET colid='63KZV' WHERE taxonid=426;
UPDATE tlkptaxon SET colid='4F98R' WHERE taxonid=427;
UPDATE tlkptaxon SET colid='625KL' WHERE taxonid=428;
UPDATE tlkptaxon SET colid='63MMR' WHERE taxonid=429;
UPDATE tlkptaxon SET colid='4H5YP' WHERE taxonid=432;
UPDATE tlkptaxon SET colid='4H5YZ' WHERE taxonid=430;
UPDATE tlkptaxon SET colid='7MF94' WHERE taxonid=431;
UPDATE tlkptaxon SET colid='6Q7C' WHERE taxonid=433;
UPDATE tlkptaxon SET colid='4HPZF' WHERE taxonid=434;
UPDATE tlkptaxon SET colid='4HPZT' WHERE taxonid=435;
UPDATE tlkptaxon SET colid='4HQ23' WHERE taxonid=438;
UPDATE tlkptaxon SET colid='6VJ27' WHERE taxonid=439;
UPDATE tlkptaxon SET colid='4HQ2J' WHERE taxonid=440;
UPDATE tlkptaxon SET colid='4HQ2V' WHERE taxonid=441;
UPDATE tlkptaxon SET colid='4HQ2X' WHERE taxonid=442;
UPDATE tlkptaxon SET colid='4HQ39' WHERE taxonid=443;
UPDATE tlkptaxon SET colid='6VJ29' WHERE taxonid=436;
UPDATE tlkptaxon SET colid='7MF6S' WHERE taxonid=437;
UPDATE tlkptaxon SET colid='4HQ3K' WHERE taxonid=444;
UPDATE tlkptaxon SET colid='4HQ46' WHERE taxonid=445;
UPDATE tlkptaxon SET colid='4HQ47' WHERE taxonid=446;
UPDATE tlkptaxon SET colid='4HQ48' WHERE taxonid=447;
UPDATE tlkptaxon SET colid='4HQ4J' WHERE taxonid=448;
UPDATE tlkptaxon SET colid='4HQ4L' WHERE taxonid=449;
UPDATE tlkptaxon SET colid='4HQ4Q' WHERE taxonid=450;
UPDATE tlkptaxon SET colid='6VJ2C' WHERE taxonid=451;
UPDATE tlkptaxon SET colid='7KMH9' WHERE taxonid=744;
UPDATE tlkptaxon SET colid='4HQ52' WHERE taxonid=452;
UPDATE tlkptaxon SET colid='6QF5' WHERE taxonid=453;
UPDATE tlkptaxon SET colid='4HTLM' WHERE taxonid=454;
UPDATE tlkptaxon SET colid='625M7' WHERE taxonid=5;
UPDATE tlkptaxon SET colid='623FD' WHERE taxonid=4;
UPDATE tlkptaxon SET colid='GG' WHERE taxonid=3;
UPDATE tlkptaxon SET colid='6QPY' WHERE taxonid=455;
UPDATE tlkptaxon SET colid='4J224' WHERE taxonid=456;
UPDATE tlkptaxon SET colid='4J22N' WHERE taxonid=457;
UPDATE tlkptaxon SET colid='4J22P' WHERE taxonid=1008;
UPDATE tlkptaxon SET colid='5QR6F' WHERE taxonid=1018;
UPDATE tlkptaxon SET colid='4J22Q' WHERE taxonid=458;
UPDATE tlkptaxon SET colid='4J22X' WHERE taxonid=1019;
UPDATE tlkptaxon SET colid='4J234' WHERE taxonid=459;
UPDATE tlkptaxon SET colid='4J237' WHERE taxonid=460;
UPDATE tlkptaxon SET colid='77KTL' WHERE taxonid=461;
UPDATE tlkptaxon SET colid='4J23W' WHERE taxonid=462;
UPDATE tlkptaxon SET colid='4J245' WHERE taxonid=463;
UPDATE tlkptaxon SET colid='4J246' WHERE taxonid=745;
UPDATE tlkptaxon SET colid='4J24K' WHERE taxonid=479;
UPDATE tlkptaxon SET colid='5KG8Z' WHERE taxonid=480;
UPDATE tlkptaxon SET colid='4J24Y' WHERE taxonid=464;
UPDATE tlkptaxon SET colid='4J254' WHERE taxonid=465;
UPDATE tlkptaxon SET colid='4J25L' WHERE taxonid=466;
UPDATE tlkptaxon SET colid='4J25P' WHERE taxonid=467;
UPDATE tlkptaxon SET colid='4J266' WHERE taxonid=1020;
UPDATE tlkptaxon SET colid='4J267' WHERE taxonid=468;
UPDATE tlkptaxon SET colid='4J269' WHERE taxonid=469;
UPDATE tlkptaxon SET colid='77KTN' WHERE taxonid=470;
UPDATE tlkptaxon SET colid='4J26G' WHERE taxonid=1021;
UPDATE tlkptaxon SET colid='4J273' WHERE taxonid=471;
UPDATE tlkptaxon SET colid='6VL5M' WHERE taxonid=472;
UPDATE tlkptaxon SET colid='4J27Y' WHERE taxonid=473;
UPDATE tlkptaxon SET colid='4J285' WHERE taxonid=474;
UPDATE tlkptaxon SET colid='4J287' WHERE taxonid=475;
UPDATE tlkptaxon SET colid='77L5M' WHERE taxonid=1022;
UPDATE tlkptaxon SET colid='77KTP' WHERE taxonid=476;
UPDATE tlkptaxon SET colid='4J29G' WHERE taxonid=477;
UPDATE tlkptaxon SET colid='4J29P' WHERE taxonid=478;
UPDATE tlkptaxon SET colid='4J29V' WHERE taxonid=481;
UPDATE tlkptaxon SET colid='6VL5T' WHERE taxonid=482;
UPDATE tlkptaxon SET colid='5QRBQ' WHERE taxonid=483;
UPDATE tlkptaxon SET colid='77L64' WHERE taxonid=484;
UPDATE tlkptaxon SET colid='4J2B2' WHERE taxonid=1023;
UPDATE tlkptaxon SET colid='4J2BY' WHERE taxonid=485;
UPDATE tlkptaxon SET colid='4J2C7' WHERE taxonid=488;
UPDATE tlkptaxon SET colid='4J2CG' WHERE taxonid=489;
UPDATE tlkptaxon SET colid='4J2KB' WHERE taxonid=507;
UPDATE tlkptaxon SET colid='4J2CJ' WHERE taxonid=490;
UPDATE tlkptaxon SET colid='4J2D5' WHERE taxonid=492;
UPDATE tlkptaxon SET colid='7KLV8' WHERE taxonid=493;
UPDATE tlkptaxon SET colid='4J2DP' WHERE taxonid=491;
UPDATE tlkptaxon SET colid='4J2DX' WHERE taxonid=494;
UPDATE tlkptaxon SET colid='77L6F' WHERE taxonid=495;
UPDATE tlkptaxon SET colid='77KSH' WHERE taxonid=486;
UPDATE tlkptaxon SET colid='5KGB8' WHERE taxonid=487;
UPDATE tlkptaxon SET colid='77KSK' WHERE taxonid=496;
UPDATE tlkptaxon SET colid='4J2F3' WHERE taxonid=497;
UPDATE tlkptaxon SET colid='4J2FG' WHERE taxonid=2009;
UPDATE tlkptaxon SET colid='4J2FH' WHERE taxonid=498;
UPDATE tlkptaxon SET colid='4J2FK' WHERE taxonid=499;
UPDATE tlkptaxon SET colid='4J2FN' WHERE taxonid=500;
UPDATE tlkptaxon SET colid='4J2FP' WHERE taxonid=501;
UPDATE tlkptaxon SET colid='4J2G9' WHERE taxonid=1009;
UPDATE tlkptaxon SET colid='4J2GF' WHERE taxonid=502;
UPDATE tlkptaxon SET colid='4J2HD' WHERE taxonid=1000;
UPDATE tlkptaxon SET colid='4J2HX' WHERE taxonid=503;
UPDATE tlkptaxon SET colid='4J2HY' WHERE taxonid=810;
UPDATE tlkptaxon SET colid='4J2J5' WHERE taxonid=504;
UPDATE tlkptaxon SET colid='4J2J7' WHERE taxonid=746;
UPDATE tlkptaxon SET colid='4J2JP' WHERE taxonid=1024;
UPDATE tlkptaxon SET colid='4J2JV' WHERE taxonid=505;
UPDATE tlkptaxon SET colid='4J2JZ' WHERE taxonid=506;
UPDATE tlkptaxon SET colid='4J2KR' WHERE taxonid=508;
UPDATE tlkptaxon SET colid='4J2KS' WHERE taxonid=509;
UPDATE tlkptaxon SET colid='6QWP' WHERE taxonid=512;
UPDATE tlkptaxon SET colid='6VMGL' WHERE taxonid=513;
UPDATE tlkptaxon SET colid='6QX7' WHERE taxonid=514;
UPDATE tlkptaxon SET colid='4J9TB' WHERE taxonid=515;
UPDATE tlkptaxon SET colid='77MJ8' WHERE taxonid=516;
UPDATE tlkptaxon SET colid='P' WHERE taxonid=1;
UPDATE tlkptaxon SET colid='6262P' WHERE taxonid=517;
UPDATE tlkptaxon SET colid='6RKR' WHERE taxonid=518;
UPDATE tlkptaxon SET colid='4JN3N' WHERE taxonid=519;
UPDATE tlkptaxon SET colid='6VNXT' WHERE taxonid=520;
UPDATE tlkptaxon SET colid='6RML' WHERE taxonid=522;
UPDATE tlkptaxon SET colid='6VPND' WHERE taxonid=523;
UPDATE tlkptaxon SET colid='6RQZ' WHERE taxonid=678;
UPDATE tlkptaxon SET colid='4JPPV' WHERE taxonid=679;
UPDATE tlkptaxon SET colid='627HD' WHERE taxonid=245;
UPDATE tlkptaxon SET colid='6TC9' WHERE taxonid=526;
UPDATE tlkptaxon SET colid='4KQ24' WHERE taxonid=527;
UPDATE tlkptaxon SET colid='6VQWN' WHERE taxonid=528;
UPDATE tlkptaxon SET colid='77QWN' WHERE taxonid=529;
UPDATE tlkptaxon SET colid='4KQ5F' WHERE taxonid=530;
UPDATE tlkptaxon SET colid='4KQ5H' WHERE taxonid=748;
UPDATE tlkptaxon SET colid='4KQ5T' WHERE taxonid=531;
UPDATE tlkptaxon SET colid='4KQ7J' WHERE taxonid=532;
UPDATE tlkptaxon SET colid='63P67' WHERE taxonid=751;
UPDATE tlkptaxon SET colid='4LLVQ' WHERE taxonid=752;
UPDATE tlkptaxon SET colid='63P5N' WHERE taxonid=1095;
UPDATE tlkptaxon SET colid='4LLZJ' WHERE taxonid=1096;
UPDATE tlkptaxon SET colid='63PVP' WHERE taxonid=535;
UPDATE tlkptaxon SET colid='4LVJ5' WHERE taxonid=536;
UPDATE tlkptaxon SET colid='77VW4' WHERE taxonid=537;
UPDATE tlkptaxon SET colid='4LVJS' WHERE taxonid=538;
UPDATE tlkptaxon SET colid='5KLW2' WHERE taxonid=544;
UPDATE tlkptaxon SET colid='4LVKV' WHERE taxonid=539;
UPDATE tlkptaxon SET colid='6W7W3' WHERE taxonid=540;
UPDATE tlkptaxon SET colid='4LVLR' WHERE taxonid=541;
UPDATE tlkptaxon SET colid='4LVM8' WHERE taxonid=1069;
UPDATE tlkptaxon SET colid='4LVNQ' WHERE taxonid=542;
UPDATE tlkptaxon SET colid='4LVR2' WHERE taxonid=543;
UPDATE tlkptaxon SET colid='6V9R' WHERE taxonid=345;
UPDATE tlkptaxon SET colid='6W7FG' WHERE taxonid=346;
UPDATE tlkptaxon SET colid='5KMHN' WHERE taxonid=347;
UPDATE tlkptaxon SET colid='6X78' WHERE taxonid=545;
UPDATE tlkptaxon SET colid='4MW5Q' WHERE taxonid=1043;
UPDATE tlkptaxon SET colid='4MW6R' WHERE taxonid=546;
UPDATE tlkptaxon SET colid='789K8' WHERE taxonid=547;
UPDATE tlkptaxon SET colid='7MHHZ' WHERE taxonid=548;
UPDATE tlkptaxon SET colid='6Y68' WHERE taxonid=549;
UPDATE tlkptaxon SET colid='4N89W' WHERE taxonid=550;
UPDATE tlkptaxon SET colid='4N89Z' WHERE taxonid=747;
UPDATE tlkptaxon SET colid='6VYM8' WHERE taxonid=551;
UPDATE tlkptaxon SET colid='6Y6H' WHERE taxonid=552;
UPDATE tlkptaxon SET colid='77ZQW' WHERE taxonid=553;
UPDATE tlkptaxon SET colid='77ZRD' WHERE taxonid=554;
UPDATE tlkptaxon SET colid='4N97Q' WHERE taxonid=1012;
UPDATE tlkptaxon SET colid='73Y7' WHERE taxonid=555;
UPDATE tlkptaxon SET colid='78HCK' WHERE taxonid=556;
UPDATE tlkptaxon SET colid='78HCJ' WHERE taxonid=557;
UPDATE tlkptaxon SET colid='6WHCL' WHERE taxonid=558;
UPDATE tlkptaxon SET colid='742N' WHERE taxonid=559;
UPDATE tlkptaxon SET colid='6WJ73' WHERE taxonid=560;
UPDATE tlkptaxon SET colid='74N7' WHERE taxonid=561;
UPDATE tlkptaxon SET colid='4PVGN' WHERE taxonid=562;
UPDATE tlkptaxon SET colid='4PVHJ' WHERE taxonid=1049;
UPDATE tlkptaxon SET colid='75K9' WHERE taxonid=563;
UPDATE tlkptaxon SET colid='4QKRX' WHERE taxonid=564;
UPDATE tlkptaxon SET colid='76HN' WHERE taxonid=565;
UPDATE tlkptaxon SET colid='4R47F' WHERE taxonid=566;
UPDATE tlkptaxon SET colid='4R47Z' WHERE taxonid=567;
UPDATE tlkptaxon SET colid='6X3QQ' WHERE taxonid=1004;
UPDATE tlkptaxon SET colid='793QP' WHERE taxonid=568;
UPDATE tlkptaxon SET colid='4R4FG' WHERE taxonid=571;
UPDATE tlkptaxon SET colid='4R4HG' WHERE taxonid=574;
UPDATE tlkptaxon SET colid='4R4KH' WHERE taxonid=803;
UPDATE tlkptaxon SET colid='4R4LC' WHERE taxonid=1003;
UPDATE tlkptaxon SET colid='4R4M7' WHERE taxonid=573;
UPDATE tlkptaxon SET colid='6X435' WHERE taxonid=575;
UPDATE tlkptaxon SET colid='4R4NR' WHERE taxonid=577;
UPDATE tlkptaxon SET colid='793R5' WHERE taxonid=578;
UPDATE tlkptaxon SET colid='4R4TP' WHERE taxonid=579;
UPDATE tlkptaxon SET colid='4R4V3' WHERE taxonid=580;
UPDATE tlkptaxon SET colid='4R4VG' WHERE taxonid=581;
UPDATE tlkptaxon SET colid='4R4WQ' WHERE taxonid=582;
UPDATE tlkptaxon SET colid='4R4YG' WHERE taxonid=576;
UPDATE tlkptaxon SET colid='4R4Z8' WHERE taxonid=1002;
UPDATE tlkptaxon SET colid='6WRQZ' WHERE taxonid=583;
UPDATE tlkptaxon SET colid='4R53K' WHERE taxonid=584;
UPDATE tlkptaxon SET colid='4R54H' WHERE taxonid=585;
UPDATE tlkptaxon SET colid='4R56Y' WHERE taxonid=1005;
UPDATE tlkptaxon SET colid='78RPF' WHERE taxonid=569;
UPDATE tlkptaxon SET colid='5KTR7' WHERE taxonid=570;
UPDATE tlkptaxon SET colid='4R597' WHERE taxonid=586;
UPDATE tlkptaxon SET colid='5KTRB' WHERE taxonid=589;
UPDATE tlkptaxon SET colid='78RPJ' WHERE taxonid=572;
UPDATE tlkptaxon SET colid='4R5DZ' WHERE taxonid=587;
UPDATE tlkptaxon SET colid='4R5F6' WHERE taxonid=588;
UPDATE tlkptaxon SET colid='4R5FS' WHERE taxonid=1014;
UPDATE tlkptaxon SET colid='4R5GR' WHERE taxonid=590;
UPDATE tlkptaxon SET colid='4R5J6' WHERE taxonid=591;
UPDATE tlkptaxon SET colid='4R5KF' WHERE taxonid=592;
UPDATE tlkptaxon SET colid='5KTSB' WHERE taxonid=593;
UPDATE tlkptaxon SET colid='4R5KM' WHERE taxonid=1010;
UPDATE tlkptaxon SET colid='4R5L5' WHERE taxonid=594;
UPDATE tlkptaxon SET colid='6X426' WHERE taxonid=595;
UPDATE tlkptaxon SET colid='4R5SK' WHERE taxonid=596;
UPDATE tlkptaxon SET colid='793Q8' WHERE taxonid=597;
UPDATE tlkptaxon SET colid='4R5YN' WHERE taxonid=598;
UPDATE tlkptaxon SET colid='4R5Z8' WHERE taxonid=599;
UPDATE tlkptaxon SET colid='4R64C' WHERE taxonid=600;
UPDATE tlkptaxon SET colid='4R667' WHERE taxonid=601;
UPDATE tlkptaxon SET colid='4R678' WHERE taxonid=602;
UPDATE tlkptaxon SET colid='7944S' WHERE taxonid=1028;
UPDATE tlkptaxon SET colid='3XS' WHERE taxonid=717;
UPDATE tlkptaxon SET colid='FN6' WHERE taxonid=182;
UPDATE tlkptaxon SET colid='78CD' WHERE taxonid=609;
UPDATE tlkptaxon SET colid='4RZYQ' WHERE taxonid=610;
UPDATE tlkptaxon SET colid='4RZZQ' WHERE taxonid=611;
UPDATE tlkptaxon SET colid='63QFF' WHERE taxonid=612;
UPDATE tlkptaxon SET colid='4SN87' WHERE taxonid=613;
UPDATE tlkptaxon SET colid='63SB8' WHERE taxonid=1090;
UPDATE tlkptaxon SET colid='4T49K' WHERE taxonid=1091;
UPDATE tlkptaxon SET colid='63S3X' WHERE taxonid=614;
UPDATE tlkptaxon SET colid='4T7YK' WHERE taxonid=615;
UPDATE tlkptaxon SET colid='4T7YV' WHERE taxonid=616;
UPDATE tlkptaxon SET colid='FTK' WHERE taxonid=65;
UPDATE tlkptaxon SET colid='3Z6' WHERE taxonid=64;
UPDATE tlkptaxon SET colid='6278P' WHERE taxonid=162;
UPDATE tlkptaxon SET colid='FWG' WHERE taxonid=534;
UPDATE tlkptaxon SET colid='7BWB' WHERE taxonid=622;
UPDATE tlkptaxon SET colid='6XCK4' WHERE taxonid=624;
UPDATE tlkptaxon SET colid='6XCJ5' WHERE taxonid=623;
UPDATE tlkptaxon SET colid='79CBB' WHERE taxonid=626;
UPDATE tlkptaxon SET colid='6XCBJ' WHERE taxonid=627;
UPDATE tlkptaxon SET colid='6XCBP' WHERE taxonid=628;
UPDATE tlkptaxon SET colid='6XCD9' WHERE taxonid=629;
UPDATE tlkptaxon SET colid='79C5X' WHERE taxonid=630;
UPDATE tlkptaxon SET colid='6XC73' WHERE taxonid=631;
UPDATE tlkptaxon SET colid='79C5K' WHERE taxonid=632;
UPDATE tlkptaxon SET colid='6XC94' WHERE taxonid=633;
UPDATE tlkptaxon SET colid='79BVQ' WHERE taxonid=634;
UPDATE tlkptaxon SET colid='6XC7R' WHERE taxonid=635;
UPDATE tlkptaxon SET colid='6XC9W' WHERE taxonid=636;
UPDATE tlkptaxon SET colid='6XDC4' WHERE taxonid=625;
UPDATE tlkptaxon SET colid='79DSK' WHERE taxonid=637;
UPDATE tlkptaxon SET colid='79DST' WHERE taxonid=638;
UPDATE tlkptaxon SET colid='6XDMG' WHERE taxonid=639;
UPDATE tlkptaxon SET colid='FXV' WHERE taxonid=641;
UPDATE tlkptaxon SET colid='3ZX' WHERE taxonid=640;
UPDATE tlkptaxon SET colid='7C8S' WHERE taxonid=642;
UPDATE tlkptaxon SET colid='6XJ9M' WHERE taxonid=643;
UPDATE tlkptaxon SET colid='FY3' WHERE taxonid=49;
UPDATE tlkptaxon SET colid='FY3' WHERE taxonid=1092;
UPDATE tlkptaxon SET colid='3ZY' WHERE taxonid=48;
UPDATE tlkptaxon SET colid='FY4' WHERE taxonid=344;
UPDATE tlkptaxon SET colid='643PF' WHERE taxonid=755;
UPDATE tlkptaxon SET colid='79QLB' WHERE taxonid=756;
UPDATE tlkptaxon SET colid='7CV2' WHERE taxonid=644;
UPDATE tlkptaxon SET colid='79QZ2' WHERE taxonid=645;
UPDATE tlkptaxon SET colid='G3X' WHERE taxonid=646;
UPDATE tlkptaxon SET colid='7DZN' WHERE taxonid=647;
UPDATE tlkptaxon SET colid='4VGZH' WHERE taxonid=648;
UPDATE tlkptaxon SET colid='7JJ' WHERE taxonid=421;
UPDATE tlkptaxon SET colid='3F4' WHERE taxonid=177;
UPDATE tlkptaxon SET colid='63SM7' WHERE taxonid=1067;
UPDATE tlkptaxon SET colid='4WP3C' WHERE taxonid=1068;
UPDATE tlkptaxon SET colid='63SMF' WHERE taxonid=649;
UPDATE tlkptaxon SET colid='4WSQG' WHERE taxonid=650;
UPDATE tlkptaxon SET colid='7FQF' WHERE taxonid=651;
UPDATE tlkptaxon SET colid='4WSQK' WHERE taxonid=652;
UPDATE tlkptaxon SET colid='7GFX' WHERE taxonid=654;
UPDATE tlkptaxon SET colid='4X52Q' WHERE taxonid=655;
UPDATE tlkptaxon SET colid='6279Z' WHERE taxonid=84;
UPDATE tlkptaxon SET colid='6466Y' WHERE taxonid=1073;
UPDATE tlkptaxon SET colid='4Z8N4' WHERE taxonid=1074;
UPDATE tlkptaxon SET colid='7N5T' WHERE taxonid=1045;
UPDATE tlkptaxon SET colid='52D9H' WHERE taxonid=1082;
UPDATE tlkptaxon SET colid='52DLK' WHERE taxonid=1046;
UPDATE tlkptaxon SET colid='7QKG' WHERE taxonid=656;
UPDATE tlkptaxon SET colid='53JDN' WHERE taxonid=657;
UPDATE tlkptaxon SET colid='7QL2' WHERE taxonid=658;
UPDATE tlkptaxon SET colid='53K5Y' WHERE taxonid=659;
UPDATE tlkptaxon SET colid='53K5Z' WHERE taxonid=660;
UPDATE tlkptaxon SET colid='7RQF' WHERE taxonid=2013;
UPDATE tlkptaxon SET colid='GWM' WHERE taxonid=662;
UPDATE tlkptaxon SET colid='7S9Q' WHERE taxonid=1065;
UPDATE tlkptaxon SET colid='54M92' WHERE taxonid=1066;
UPDATE tlkptaxon SET colid='7S9V' WHERE taxonid=663;
UPDATE tlkptaxon SET colid='54M9Z' WHERE taxonid=757;
UPDATE tlkptaxon SET colid='7BJ8N' WHERE taxonid=664;
UPDATE tlkptaxon SET colid='GXW' WHERE taxonid=669;
UPDATE tlkptaxon SET colid='7T6Q' WHERE taxonid=665;
UPDATE tlkptaxon SET colid='5526G' WHERE taxonid=666;
UPDATE tlkptaxon SET colid='7N2BM' WHERE taxonid=667;
UPDATE tlkptaxon SET colid='5526P' WHERE taxonid=668;
UPDATE tlkptaxon SET colid='63TKC' WHERE taxonid=670;
UPDATE tlkptaxon SET colid='55284' WHERE taxonid=671;
UPDATE tlkptaxon SET colid='7T9P' WHERE taxonid=672;
UPDATE tlkptaxon SET colid='553LY' WHERE taxonid=673;
UPDATE tlkptaxon SET colid='647GM' WHERE taxonid=1059;
UPDATE tlkptaxon SET colid='55G35' WHERE taxonid=1072;
UPDATE tlkptaxon SET colid='55G93' WHERE taxonid=1060;
UPDATE tlkptaxon SET colid='64736' WHERE taxonid=674;
UPDATE tlkptaxon SET colid='55LNG' WHERE taxonid=675;
UPDATE tlkptaxon SET colid='H5S' WHERE taxonid=303;
UPDATE tlkptaxon SET colid='648D5' WHERE taxonid=676;
UPDATE tlkptaxon SET colid='56NTP' WHERE taxonid=677;
UPDATE tlkptaxon SET colid='56NTV' WHERE taxonid=680;
UPDATE tlkptaxon SET colid='56NTZ' WHERE taxonid=681;
UPDATE tlkptaxon SET colid='63WD4' WHERE taxonid=682;
UPDATE tlkptaxon SET colid='7C9LX' WHERE taxonid=683;
UPDATE tlkptaxon SET colid='7WX3' WHERE taxonid=685;
UPDATE tlkptaxon SET colid='56WNF' WHERE taxonid=686;
UPDATE tlkptaxon SET colid='56WPP' WHERE taxonid=687;
UPDATE tlkptaxon SET colid='7CC2T' WHERE taxonid=688;
UPDATE tlkptaxon SET colid='CDB' WHERE taxonid=684;
UPDATE tlkptaxon SET colid='7XWB' WHERE taxonid=689;
UPDATE tlkptaxon SET colid='57GND' WHERE taxonid=690;
UPDATE tlkptaxon SET colid='7YPK' WHERE taxonid=2012;
UPDATE tlkptaxon SET colid='7Z63' WHERE taxonid=1083;
UPDATE tlkptaxon SET colid='588Q2' WHERE taxonid=1084;
UPDATE tlkptaxon SET colid='6486F' WHERE taxonid=691;
UPDATE tlkptaxon SET colid='59HM6' WHERE taxonid=692;
UPDATE tlkptaxon SET colid='59HM7' WHERE taxonid=693;
UPDATE tlkptaxon SET colid='59HM8' WHERE taxonid=694;
UPDATE tlkptaxon SET colid='7D9VC' WHERE taxonid=695;
UPDATE tlkptaxon SET colid='59HMC' WHERE taxonid=696;
UPDATE tlkptaxon SET colid='59HMG' WHERE taxonid=697;
UPDATE tlkptaxon SET colid='59HMR' WHERE taxonid=698;
UPDATE tlkptaxon SET colid='59HMW' WHERE taxonid=699;
UPDATE tlkptaxon SET colid='HMT' WHERE taxonid=808;
UPDATE tlkptaxon SET colid='HNB' WHERE taxonid=194;
UPDATE tlkptaxon SET colid='84MS' WHERE taxonid=700;
UPDATE tlkptaxon SET colid='7DFJZ' WHERE taxonid=701;
UPDATE tlkptaxon SET colid='7DFMR' WHERE taxonid=702;
UPDATE tlkptaxon SET colid='7DFP7' WHERE taxonid=703;
UPDATE tlkptaxon SET colid='7DFPH' WHERE taxonid=704;
UPDATE tlkptaxon SET colid='85LM' WHERE taxonid=1080;
UPDATE tlkptaxon SET colid='7F98J' WHERE taxonid=2003;
UPDATE tlkptaxon SET colid='7F8VF' WHERE taxonid=45;
UPDATE tlkptaxon SET colid='7F8XX' WHERE taxonid=1081;
UPDATE tlkptaxon SET colid='7F8YD' WHERE taxonid=2002;
UPDATE tlkptaxon SET colid='7F995' WHERE taxonid=46;
UPDATE tlkptaxon SET colid='5LQ69' WHERE taxonid=47;
UPDATE tlkptaxon SET colid='HST' WHERE taxonid=217;
UPDATE tlkptaxon SET colid='86MY' WHERE taxonid=758;
UPDATE tlkptaxon SET colid='5BC9Y' WHERE taxonid=759;
UPDATE tlkptaxon SET colid='6272Z' WHERE taxonid=1089;
UPDATE tlkptaxon SET colid='647Q2' WHERE taxonid=705;
UPDATE tlkptaxon SET colid='5BRJV' WHERE taxonid=706;
UPDATE tlkptaxon SET colid='87X6' WHERE taxonid=707;
UPDATE tlkptaxon SET colid='5BYG9' WHERE taxonid=708;
UPDATE tlkptaxon SET colid='6274R' WHERE taxonid=263;
UPDATE tlkptaxon SET colid='649GY' WHERE taxonid=208;
UPDATE tlkptaxon SET colid='5C5GD' WHERE taxonid=209;
UPDATE tlkptaxon SET colid='648P5' WHERE taxonid=1029;
UPDATE tlkptaxon SET colid='5D5ZY' WHERE taxonid=1039;
UPDATE tlkptaxon SET colid='5D648' WHERE taxonid=1030;
UPDATE tlkptaxon SET colid='J39' WHERE taxonid=709;
UPDATE tlkptaxon SET colid='8BDZ' WHERE taxonid=710;

UPDATE tlkptaxon SET colid='4J2CF' WHERE taxonid=2016;
UPDATE tlkptaxon SET colid='8KC2' WHERE taxonid=2017;
UPDATE tlkptaxon SET colid='T8' WHERE taxonid=2018;
UPDATE tlkptaxon SET colid='649' WHERE taxonid=2019;
UPDATE tlkptaxon SET colid='PXQ' WHERE taxonid=2020;
UPDATE tlkptaxon SET colid='65DRF' WHERE taxonid=2021;

-- Rank changes
UPDATE tlkptaxon SET colid='JP8', taxonrankid=19 where taxonid=71;

-- Update labels and parent IDs 
UPDATE tlkptaxon SET newlabel='Milicia excelsa (Welw.) C. C. Berg', htmllabel='<i>Milicia excelsa</i> (Welw.) C. C. Berg', colparentid='5SSS' WHERE colid='43CGC';



UPDATE tlkptaxon SET newlabel='Ebenaceae', htmllabel='<i>Pinus montezumae</i> Lamb.', colparentid='6QPY' WHERE colid='4J2CF';
UPDATE tlkptaxon SET newlabel='Ebenaceae', htmllabel='<i>Abies durangensis</i> Martínez', colparentid='627WF' WHERE colid='8KC2';
UPDATE tlkptaxon SET newlabel='Ebenaceae', htmllabel='Berberidopsidales Doweld', colparentid='40' WHERE colid='T8';
UPDATE tlkptaxon SET newlabel='Ebenaceae', htmllabel='Aextoxicaceae Engl. & Gilg', colparentid='T8' WHERE colid='649';
UPDATE tlkptaxon SET newlabel='Ebenaceae', htmllabel='<i>Aextoxicon</i> Ruiz & Pav.', colparentid='649' WHERE colid='PXQ';
UPDATE tlkptaxon SET newlabel='Ebenaceae', htmllabel='<i>Aextoxicon punctatum</i> Ruiz & Pav.', colparentid='PXQ' WHERE colid='65DRF';

UPDATE tlkptaxon SET newlabel='Ebenaceae', htmllabel='Ebenaceae', colparentid='625QY' WHERE colid='9JQ';
UPDATE tlkptaxon SET newlabel='Hippocastanoideae Burnett', htmllabel='Hippocastanoideae Burnett', colparentid='FY3' WHERE colid='JP8';
UPDATE tlkptaxon SET newlabel='Platanus racemosa Nutt. ex Audubon', htmllabel='<i>Platanus racemosa</i> Nutt. ex Audubon', colparentid='6RKR' WHERE colid='6VNXT';
UPDATE tlkptaxon SET newlabel='Pouteria glomerata (Miq.) Radlk.', htmllabel='<i>Pouteria glomerata</i> (Miq.) Radlk.', colparentid='6V9R' WHERE colid='6W7FG';
UPDATE tlkptaxon SET newlabel='Rosales Bercht. & J. Presl', htmllabel='Rosales Bercht. & J. Presl', colparentid='MG' WHERE colid='3Z6';
UPDATE tlkptaxon SET newlabel='Rosaceae Juss.', htmllabel='Rosaceae Juss.', colparentid='3Z6' WHERE colid='FTK';
UPDATE tlkptaxon SET newlabel='Plantae', htmllabel='Plantae', colparentid='5T6MX' WHERE colid='P';
UPDATE tlkptaxon SET newlabel='Abies fargesii var. faxoniana (Rehd. & E.H. Wilson) T. S. Liu', htmllabel='<i>Abies fargesii</i> var. <i>faxoniana</i> (Rehd. & E.H. Wilson) T. S. Liu', colparentid='8KCF' WHERE colid='5LWNW';
UPDATE tlkptaxon SET newlabel='Abies fraseri (Pursh) Poir.', htmllabel='<i>Abies fraseri</i> (Pursh) Poir.', colparentid='627WF' WHERE colid='8KCQ';
UPDATE tlkptaxon SET newlabel='Abies holophylla Maxim.', htmllabel='<i>Abies holophylla</i> Maxim.', colparentid='627WF' WHERE colid='63Z5X';
UPDATE tlkptaxon SET newlabel='Abies kawakamii (Hayata) T. Itô', htmllabel='<i>Abies kawakamii</i> (Hayata) T. Itô', colparentid='627WF' WHERE colid='8KDR';
UPDATE tlkptaxon SET newlabel='n/a', htmllabel='n/a', colparentid='n/a' WHERE colid='';
UPDATE tlkptaxon SET newlabel='Abies koreana E.H. Wilson', htmllabel='<i>Abies koreana</i> E.H. Wilson', colparentid='627WF' WHERE colid='8KDT';
UPDATE tlkptaxon SET newlabel='Abies magnifica A. Murray bis', htmllabel='<i>Abies magnifica</i> A. Murray bis', colparentid='627WF' WHERE colid='63Z6H';
UPDATE tlkptaxon SET newlabel='Abies nebrodensis (Lojac.) Mattei', htmllabel='<i>Abies nebrodensis</i> (Lojac.) Mattei', colparentid='627WF' WHERE colid='63YVP';
UPDATE tlkptaxon SET newlabel='Abies nephrolepis (Trautv. ex Maxim.) Maxim.', htmllabel='<i>Abies nephrolepis</i> (Trautv. ex Maxim.) Maxim.', colparentid='627WF' WHERE colid='63YW4';
UPDATE tlkptaxon SET newlabel='Abies numidica de Lannoy ex Carrière', htmllabel='<i>Abies numidica</i> de Lannoy ex Carrière', colparentid='627WF' WHERE colid='8KF3';
UPDATE tlkptaxon SET newlabel='Abies pindrow (Royle ex D. Don) Royle', htmllabel='<i>Abies pindrow</i> (Royle ex D. Don) Royle', colparentid='627WF' WHERE colid='8KFL';
UPDATE tlkptaxon SET newlabel='Abies pinsapo Boiss.', htmllabel='<i>Abies pinsapo</i> Boiss.', colparentid='627WF' WHERE colid='8KFM';
UPDATE tlkptaxon SET newlabel='Abies pinsapo var. marocana (Trab.) Ceballos & Bolaño', htmllabel='<i>Abies pinsapo</i> var. <i>marocana</i> (Trab.) Ceballos & Bolaño', colparentid='8KFM' WHERE colid='5LWQW';
UPDATE tlkptaxon SET newlabel='Abies procera Rehd.', htmllabel='<i>Abies procera</i> Rehd.', colparentid='627WF' WHERE colid='8KFP';
UPDATE tlkptaxon SET newlabel='Abies recurvata Mast.', htmllabel='<i>Abies recurvata</i> Mast.', colparentid='627WF' WHERE colid='8KFR';
UPDATE tlkptaxon SET newlabel='Abies religiosa (Kunth) Schltdl. & Cham.', htmllabel='<i>Abies religiosa</i> (Kunth) Schltdl. & Cham.', colparentid='627WF' WHERE colid='8KFT';
UPDATE tlkptaxon SET newlabel='Abies sachalinensis (F. Schmidt) Mast.', htmllabel='<i>Abies sachalinensis</i> (F. Schmidt) Mast.', colparentid='627WF' WHERE colid='8KFW';
UPDATE tlkptaxon SET newlabel='Abies sibirica Ledeb.', htmllabel='<i>Abies sibirica</i> Ledeb.', colparentid='627WF' WHERE colid='8KG5';
UPDATE tlkptaxon SET newlabel='Abies spectabilis (D. Don) Mirb.', htmllabel='<i>Abies spectabilis</i> (D. Don) Mirb.', colparentid='627WF' WHERE colid='63YW5';
UPDATE tlkptaxon SET newlabel='Abies squamata Mast.', htmllabel='<i>Abies squamata</i> Mast.', colparentid='627WF' WHERE colid='8KGC';
UPDATE tlkptaxon SET newlabel='Acacia', htmllabel='<i>Acacia</i>', colparentid='623QT' WHERE colid='LV7';
UPDATE tlkptaxon SET newlabel='Acacia alpina F.Muell.', htmllabel='<i>Acacia alpina</i> F.Muell.', colparentid='LV7' WHERE colid='8NR9';
UPDATE tlkptaxon SET newlabel='Acer campestre L.', htmllabel='<i>Acer campestre</i> L.', colparentid='MLD' WHERE colid='946Z';
UPDATE tlkptaxon SET newlabel='Acer L.', htmllabel='<i>Acer</i> L.', colparentid='JP8' WHERE colid='MLD';
UPDATE tlkptaxon SET newlabel='Acer negundo L.', htmllabel='<i>Acer negundo</i> L.', colparentid='MLD' WHERE colid='94F5';
UPDATE tlkptaxon SET newlabel='Acer pensylvanicum L.', htmllabel='<i>Acer pensylvanicum</i> L.', colparentid='MLD' WHERE colid='94GR';
UPDATE tlkptaxon SET newlabel='Acer platanoides L.', htmllabel='<i>Acer platanoides</i> L.', colparentid='MLD' WHERE colid='94H3';
UPDATE tlkptaxon SET newlabel='Acer pseudoplatanus L.', htmllabel='<i>Acer pseudoplatanus</i> L.', colparentid='MLD' WHERE colid='94HF';
UPDATE tlkptaxon SET newlabel='Acer rubrum L.', htmllabel='<i>Acer rubrum</i> L.', colparentid='MLD' WHERE colid='94JD';
UPDATE tlkptaxon SET newlabel='Acer saccharinum L.', htmllabel='<i>Acer saccharinum</i> L.', colparentid='MLD' WHERE colid='94JH';
UPDATE tlkptaxon SET newlabel='Acer saccharum Marshall', htmllabel='<i>Acer saccharum</i> Marshall', colparentid='MLD' WHERE colid='94JK';
UPDATE tlkptaxon SET newlabel='Acer spicatum Lam.', htmllabel='<i>Acer spicatum</i> Lam.', colparentid='MLD' WHERE colid='64FL7';
UPDATE tlkptaxon SET newlabel='Adansonia digitata L.', htmllabel='<i>Adansonia digitata</i> L.', colparentid='NYL' WHERE colid='9X63';
UPDATE tlkptaxon SET newlabel='Adansonia L.', htmllabel='<i>Adansonia</i> L.', colparentid='J8B' WHERE colid='NYL';
UPDATE tlkptaxon SET newlabel='Adenostoma Hook. & Arn.', htmllabel='<i>Adenostoma</i> Hook. & Arn.', colparentid='JV6' WHERE colid='P7R';
UPDATE tlkptaxon SET newlabel='Adesmia', htmllabel='<i>Adesmia</i>', colparentid='623QT' WHERE colid='P8K';
UPDATE tlkptaxon SET newlabel='Adesmia horrida Hook. & Arn.', htmllabel='<i>Adesmia horrida</i> Hook. & Arn.', colparentid='P8K' WHERE colid='64QC9';
UPDATE tlkptaxon SET newlabel='Adesmia uspallatensis Hook. & Arn.', htmllabel='<i>Adesmia uspallatensis</i> Hook. & Arn.', colparentid='P8K' WHERE colid='64RWW';
UPDATE tlkptaxon SET newlabel='Aesculus hippocastanum L.', htmllabel='<i>Aesculus hippocastanum</i> L.', colparentid='PTB' WHERE colid='65BKZ';
UPDATE tlkptaxon SET newlabel='Aesculus L.', htmllabel='<i>Aesculus</i> L.', colparentid='JP8' WHERE colid='PTB';
UPDATE tlkptaxon SET newlabel='Afzelia', htmllabel='<i>Afzelia</i>', colparentid='623QT' WHERE colid='629CD';
UPDATE tlkptaxon SET newlabel='Afzelia africana Pers.', htmllabel='<i>Afzelia africana</i> Pers.', colparentid='629CD' WHERE colid='65JHK';
UPDATE tlkptaxon SET newlabel='Afzelia quanzensis Welw.', htmllabel='<i>Afzelia quanzensis</i> Welw.', colparentid='629CD' WHERE colid='65JJG';
UPDATE tlkptaxon SET newlabel='Agathis', htmllabel='<i>Agathis</i>', colparentid='6MH' WHERE colid='QM5';
UPDATE tlkptaxon SET newlabel='Agathis macrophylla (Lindl.) Mast.', htmllabel='<i>Agathis macrophylla</i> (Lindl.) Mast.', colparentid='QM5' WHERE colid='662TT';
UPDATE tlkptaxon SET newlabel='Agathis moorei (Lindl.) Mast.', htmllabel='<i>Agathis moorei</i> (Lindl.) Mast.', colparentid='QM5' WHERE colid='65QTX';
UPDATE tlkptaxon SET newlabel='Agathis ovata (C. Moore ex Vieill.) Warb.', htmllabel='<i>Agathis ovata</i> (C. Moore ex Vieill.) Warb.', colparentid='QM5' WHERE colid='662TY';
UPDATE tlkptaxon SET newlabel='Agathis robusta (C. Moore ex F. Muell.) F.M. Bailey', htmllabel='<i>Agathis robusta</i> (C. Moore ex F. Muell.) F.M. Bailey', colparentid='QM5' WHERE colid='6635Z';
UPDATE tlkptaxon SET newlabel='Ailanthus altissima (Mill.) Swingle', htmllabel='<i>Ailanthus altissima</i> (Mill.) Swingle', colparentid='R65' WHERE colid='BCBF';
UPDATE tlkptaxon SET newlabel='Ailanthus Desf.', htmllabel='<i>Ailanthus</i> Desf.', colparentid='6279Z' WHERE colid='R65';
UPDATE tlkptaxon SET newlabel='Allocasuarina L. A. S. Johnson', htmllabel='<i>Allocasuarina</i> L. A. S. Johnson', colparentid='7SM' WHERE colid='S78';
UPDATE tlkptaxon SET newlabel='Allocasuarina verticillata (Lam.) L. A. S. Johnson', htmllabel='<i>Allocasuarina verticillata</i> (Lam.) L. A. S. Johnson', colparentid='S78' WHERE colid='BVD6';
UPDATE tlkptaxon SET newlabel='Alnus alnobetula subsp. crispa (Aiton) Raus', htmllabel='<i>Alnus alnobetula</i> subsp. <i>crispa</i> (Aiton) Raus', colparentid='C2NF' WHERE colid='5FHWF';
UPDATE tlkptaxon SET newlabel='Andira', htmllabel='<i>Andira</i>', colparentid='623QT' WHERE colid='VRN';
UPDATE tlkptaxon SET newlabel='Andira coriacea Pulle', htmllabel='<i>Andira coriacea</i> Pulle', colparentid='VRN' WHERE colid='66N99';
UPDATE tlkptaxon SET newlabel='Annona', htmllabel='<i>Annona</i>', colparentid='6FB' WHERE colid='62BGN';
UPDATE tlkptaxon SET newlabel='Annona spraguei Saff.', htmllabel='<i>Annona spraguei</i> Saff.', colparentid='62BGN' WHERE colid='674CV';
UPDATE tlkptaxon SET newlabel='Annonaceae', htmllabel='Annonaceae', colparentid='3HL' WHERE colid='6FB';
UPDATE tlkptaxon SET newlabel='Araucaria', htmllabel='<i>Araucaria</i>', colparentid='6MH' WHERE colid='ZSD';
UPDATE tlkptaxon SET newlabel='Araucaria angustifolia (Bertol.) Kuntze', htmllabel='<i>Araucaria angustifolia</i> (Bertol.) Kuntze', colparentid='ZSD' WHERE colid='67PQJ';
UPDATE tlkptaxon SET newlabel='Araucaria araucana (Molina) K. Koch', htmllabel='<i>Araucaria araucana</i> (Molina) K. Koch', colparentid='ZSD' WHERE colid='G67B';
UPDATE tlkptaxon SET newlabel='Araucaria cunninghamii Aiton ex A. Cunn.', htmllabel='<i>Araucaria cunninghamii</i> Aiton ex A. Cunn.', colparentid='ZSD' WHERE colid='G67L';
UPDATE tlkptaxon SET newlabel='Araucaria heterophylla (Salisb.) Franco', htmllabel='<i>Araucaria heterophylla</i> (Salisb.) Franco', colparentid='ZSD' WHERE colid='G67T';
UPDATE tlkptaxon SET newlabel='Araucaria hunsteinii K. Schum.', htmllabel='<i>Araucaria hunsteinii</i> K. Schum.', colparentid='ZSD' WHERE colid='G67V';
UPDATE tlkptaxon SET newlabel='Araucariaceae', htmllabel='Araucariaceae', colparentid='623FD' WHERE colid='6MH';
UPDATE tlkptaxon SET newlabel='Arctostaphylos Adans.', htmllabel='<i>Arctostaphylos</i> Adans.', colparentid='J65' WHERE colid='62CT9';
UPDATE tlkptaxon SET newlabel='Arctostaphylos glauca Lindl.', htmllabel='<i>Arctostaphylos glauca</i> Lindl.', colparentid='62CT9' WHERE colid='GBMK';
UPDATE tlkptaxon SET newlabel='Artemisia L.', htmllabel='<i>Artemisia</i> L.', colparentid='J6T' WHERE colid='62CHR';
UPDATE tlkptaxon SET newlabel='Artemisia tridentata (Nutt.) W. A. Weber', htmllabel='<i>Artemisia tridentata</i> (Nutt.) W. A. Weber', colparentid='62CHR' WHERE colid='GX2B';
UPDATE tlkptaxon SET newlabel='Asteraceae Dumort.', htmllabel='Asteraceae Dumort.', colparentid='ST' WHERE colid='622TP';
UPDATE tlkptaxon SET newlabel='Asterales', htmllabel='Asterales', colparentid='MG' WHERE colid='ST';
UPDATE tlkptaxon SET newlabel='Athrotaxis cupressoides D. Don', htmllabel='<i>Athrotaxis cupressoides</i> D. Don', colparentid='352R' WHERE colid='JF6F';
UPDATE tlkptaxon SET newlabel='Athrotaxis selaginoides D. Don', htmllabel='<i>Athrotaxis selaginoides</i> D. Don', colparentid='352R' WHERE colid='JF6K';
UPDATE tlkptaxon SET newlabel='Austrocedrus', htmllabel='<i>Austrocedrus</i>', colparentid='8SY' WHERE colid='3687';
UPDATE tlkptaxon SET newlabel='Austrocedrus chilensis (D. Don) Pic. Serm. & Bizzarri', htmllabel='<i>Austrocedrus chilensis</i> (D. Don) Pic. Serm. & Bizzarri', colparentid='3687' WHERE colid='JVXR';
UPDATE tlkptaxon SET newlabel='Bertholletia', htmllabel='<i>Bertholletia</i>', colparentid='624WH' WHERE colid='39SV';
UPDATE tlkptaxon SET newlabel='Bertholletia excelsa Bonpl.', htmllabel='<i>Bertholletia excelsa</i> Bonpl.', colparentid='39SV' WHERE colid='LMPJ';
UPDATE tlkptaxon SET newlabel='Betulaceae', htmllabel='Betulaceae', colparentid='384' WHERE colid='777';
UPDATE tlkptaxon SET newlabel='Casuarinaceae R. Br.', htmllabel='Casuarinaceae R. Br.', colparentid='384' WHERE colid='7SM';
UPDATE tlkptaxon SET newlabel='Celtis L.', htmllabel='<i>Celtis</i> L.', colparentid='7NV' WHERE colid='3K3G';
UPDATE tlkptaxon SET newlabel='Cupressaceae', htmllabel='Cupressaceae', colparentid='623FD' WHERE colid='8SY';
UPDATE tlkptaxon SET newlabel='Ericales', htmllabel='Ericales', colparentid='MG' WHERE colid='625QY';
UPDATE tlkptaxon SET newlabel='Fabaceae', htmllabel='Fabaceae', colparentid='383' WHERE colid='623QT';
UPDATE tlkptaxon SET newlabel='Fabales Bromhead', htmllabel='Fabales Bromhead', colparentid='MG' WHERE colid='383';
UPDATE tlkptaxon SET newlabel='Fagales', htmllabel='Fagales', colparentid='MG' WHERE colid='384';
UPDATE tlkptaxon SET newlabel='Lecythidaceae', htmllabel='Lecythidaceae', colparentid='625QY' WHERE colid='624WH';
UPDATE tlkptaxon SET newlabel='Magnoliales', htmllabel='Magnoliales', colparentid='MG' WHERE colid='3HL';
UPDATE tlkptaxon SET newlabel='Magnoliopsida', htmllabel='Magnoliopsida', colparentid='TP' WHERE colid='MG';
UPDATE tlkptaxon SET newlabel='Malvaceae Juss.', htmllabel='Malvaceae Juss.', colparentid='3HP' WHERE colid='CDB';
UPDATE tlkptaxon SET newlabel='Pinales', htmllabel='Pinales', colparentid='GG' WHERE colid='623FD';
UPDATE tlkptaxon SET newlabel='Pinopsida', htmllabel='Pinopsida', colparentid='TP' WHERE colid='GG';
UPDATE tlkptaxon SET newlabel='Sapindaceae Juss.', htmllabel='Sapindaceae Juss.', colparentid='3ZY' WHERE colid='FY3';
UPDATE tlkptaxon SET newlabel='Sapindales Juss. ex Bercht. & J. Presl', htmllabel='Sapindales Juss. ex Bercht. & J. Presl', colparentid='MG' WHERE colid='3ZY';
UPDATE tlkptaxon SET newlabel='Simaroubaceae DC.', htmllabel='Simaroubaceae DC.', colparentid='3ZY' WHERE colid='6279Z';
UPDATE tlkptaxon SET newlabel='Vachellia tortilis (Forssk.) Galasso & Banfi', htmllabel='<i>Vachellia tortilis</i> (Forssk.) Galasso & Banfi', colparentid='85LM' WHERE colid='7F995';
UPDATE tlkptaxon SET newlabel='Vachellia tortilis subsp. raddiana (Savi) Kyal. & Boatwr.', htmllabel='<i>Vachellia tortilis</i> subsp. <i>raddiana</i> (Savi) Kyal. & Boatwr.', colparentid='7F995' WHERE colid='5LQ69';
UPDATE tlkptaxon SET newlabel='Alnus rhombifolia Nutt.', htmllabel='<i>Alnus rhombifolia</i> Nutt.', colparentid='SQX' WHERE colid='C2SM';
UPDATE tlkptaxon SET newlabel='Alnus serrulata (Aiton) Willd.', htmllabel='<i>Alnus serrulata</i> (Aiton) Willd.', colparentid='SQX' WHERE colid='C2SV';
UPDATE tlkptaxon SET newlabel='Anacardiaceae R. Br.', htmllabel='Anacardiaceae R. Br.', colparentid='3ZY' WHERE colid='6CK';
UPDATE tlkptaxon SET newlabel='Betula', htmllabel='<i>Betula</i>', colparentid='777' WHERE colid='62GLV';
UPDATE tlkptaxon SET newlabel='n/a', htmllabel='n/a', colparentid='n/a' WHERE colid='';
UPDATE tlkptaxon SET newlabel='Abies sachalinensis var. mayriana Miyabe & Kudô', htmllabel='<i>Abies sachalinensis</i> var. <i>mayriana</i> Miyabe & Kudô', colparentid='8KFW' WHERE colid='7L7J6';
UPDATE tlkptaxon SET newlabel='Afrocanthium burttii (Bullock) Lantz', htmllabel='<i>Afrocanthium burttii</i> (Bullock) Lantz', colparentid='Q4Y' WHERE colid='65FXW';
UPDATE tlkptaxon SET newlabel='Alnus incana (L.) Moench', htmllabel='<i>Alnus incana</i> (L.) Moench', colparentid='SQX' WHERE colid='5TWTV';
UPDATE tlkptaxon SET newlabel='Alnus maximowiczii Callier', htmllabel='<i>Alnus maximowiczii</i> Callier', colparentid='SQX' WHERE colid='5TX67';
UPDATE tlkptaxon SET newlabel='Betula utilis subsp. albosinensis (Burkill) Ashburner & McAll.', htmllabel='<i>Betula utilis</i> subsp. <i>albosinensis</i> (Burkill) Ashburner & McAll.', colparentid='LPG4' WHERE colid='LP4H';
UPDATE tlkptaxon SET newlabel='Betula alleghaniensis Britton', htmllabel='<i>Betula alleghaniensis</i> Britton', colparentid='62GLV' WHERE colid='LP4J';
UPDATE tlkptaxon SET newlabel='Betula ermanii Cham.', htmllabel='<i>Betula ermanii</i> Cham.', colparentid='62GLV' WHERE colid='LP7F';
UPDATE tlkptaxon SET newlabel='Betula grossa Siebold & Zucc.', htmllabel='<i>Betula grossa</i> Siebold & Zucc.', colparentid='62GLV' WHERE colid='LP8N';
UPDATE tlkptaxon SET newlabel='Betula pendula subsp. mandshurica (Regel) Ashburner & McAll.', htmllabel='<i>Betula pendula</i> subsp. <i>mandshurica</i> (Regel) Ashburner & McAll.', colparentid='LPCQ' WHERE colid='LPBT';
UPDATE tlkptaxon SET newlabel='Betula nigra L.', htmllabel='<i>Betula nigra</i> L.', colparentid='62GLV' WHERE colid='LPBV';
UPDATE tlkptaxon SET newlabel='Betula papyrifera Marshall', htmllabel='<i>Betula papyrifera</i> Marshall', colparentid='62GLV' WHERE colid='LPCK';
UPDATE tlkptaxon SET newlabel='Betula pendula Roth', htmllabel='<i>Betula pendula</i> Roth', colparentid='62GLV' WHERE colid='LPCQ';
UPDATE tlkptaxon SET newlabel='Betula pendula subsp. pendula', htmllabel='<i>Betula pendula</i> subsp. <i>pendula</i>', colparentid='LPCQ' WHERE colid='5FXVF';
UPDATE tlkptaxon SET newlabel='Betula pendula subsp. mandshurica (Regel) Ashburner & McAll.', htmllabel='<i>Betula pendula</i> subsp. <i>mandshurica</i> (Regel) Ashburner & McAll.', colparentid='LPCQ' WHERE colid='LPCX';
UPDATE tlkptaxon SET newlabel='Betula pubescens Ehrh.', htmllabel='<i>Betula pubescens</i> Ehrh.', colparentid='62GLV' WHERE colid='LPDF';
UPDATE tlkptaxon SET newlabel='Betula utilis D.Don', htmllabel='<i>Betula utilis</i> D.Don', colparentid='62GLV' WHERE colid='LPG4';
UPDATE tlkptaxon SET newlabel='Bignoniaceae Juss.', htmllabel='Bignoniaceae Juss.', colparentid='3F4' WHERE colid='77P';
UPDATE tlkptaxon SET newlabel='Bombax L.', htmllabel='<i>Bombax</i> L.', colparentid='J8B' WHERE colid='3BBF';
UPDATE tlkptaxon SET newlabel='Bombax ceiba L.', htmllabel='<i>Bombax ceiba</i> L.', colparentid='3BBF' WHERE colid='MF6V';
UPDATE tlkptaxon SET newlabel='Bursera Jacq. ex L.', htmllabel='<i>Bursera</i> Jacq. ex L.', colparentid='7GV' WHERE colid='62HRS';
UPDATE tlkptaxon SET newlabel='Bursera simaruba (L.) Sarg.', htmllabel='<i>Bursera simaruba</i> (L.) Sarg.', colparentid='62HRS' WHERE colid='NYJ6';
UPDATE tlkptaxon SET newlabel='Burseraceae Kunth', htmllabel='Burseraceae Kunth', colparentid='3ZY' WHERE colid='7GV';
UPDATE tlkptaxon SET newlabel='Buxaceae Dumort.', htmllabel='Buxaceae Dumort.', colparentid='6229G' WHERE colid='7H5';
UPDATE tlkptaxon SET newlabel='Buxus L.', htmllabel='<i>Buxus</i> L.', colparentid='J94' WHERE colid='62HXK';
UPDATE tlkptaxon SET newlabel='Buxus sempervirens L.', htmllabel='<i>Buxus sempervirens</i> L.', colparentid='62HXK' WHERE colid='5X65N';
UPDATE tlkptaxon SET newlabel='Callitris', htmllabel='<i>Callitris</i>', colparentid='8SY' WHERE colid='3FMB';
UPDATE tlkptaxon SET newlabel='Callitris columellaris F. Muell.', htmllabel='<i>Callitris columellaris</i> F. Muell.', colparentid='3FMB' WHERE colid='68W29';
UPDATE tlkptaxon SET newlabel='Callitris macleayana (F. Muell.) F. Muell.', htmllabel='<i>Callitris macleayana</i> (F. Muell.) F. Muell.', colparentid='3FMB' WHERE colid='697PB';
UPDATE tlkptaxon SET newlabel='Callitris preissii Miq.', htmllabel='<i>Callitris preissii</i> Miq.', colparentid='3FMB' WHERE colid='68VPF';
UPDATE tlkptaxon SET newlabel='Calocedrus', htmllabel='<i>Calocedrus</i>', colparentid='8SY' WHERE colid='3FS7';
UPDATE tlkptaxon SET newlabel='Calocedrus decurrens (Torr.) Florin', htmllabel='<i>Calocedrus decurrens</i> (Torr.) Florin', colparentid='3FS7' WHERE colid='5WVCR';
UPDATE tlkptaxon SET newlabel='Carpinus', htmllabel='<i>Carpinus</i>', colparentid='777' WHERE colid='3HTL';
UPDATE tlkptaxon SET newlabel='Carpinus betulus L.', htmllabel='<i>Carpinus betulus</i> L.', colparentid='3HTL' WHERE colid='RG23';
UPDATE tlkptaxon SET newlabel='Carya Nutt.', htmllabel='<i>Carya</i> Nutt.', colparentid='JRQ' WHERE colid='3HXZ';
UPDATE tlkptaxon SET newlabel='Carya alba (L.) Nutt.', htmllabel='<i>Carya alba</i> (L.) Nutt.', colparentid='3HXZ' WHERE colid='RHRN';
UPDATE tlkptaxon SET newlabel='Carya glabra (Mill.) Sweet', htmllabel='<i>Carya glabra</i> (Mill.) Sweet', colparentid='3HXZ' WHERE colid='RHS8';
UPDATE tlkptaxon SET newlabel='Castanea', htmllabel='<i>Castanea</i>', colparentid='623R7' WHERE colid='62KM4';
UPDATE tlkptaxon SET newlabel='Castanea crenata Siebold & Zucc.', htmllabel='<i>Castanea crenata</i> Siebold & Zucc.', colparentid='62KM4' WHERE colid='RNSP';
UPDATE tlkptaxon SET newlabel='Castanea dentata (Marshall) Borkh.', htmllabel='<i>Castanea dentata</i> (Marshall) Borkh.', colparentid='62KM4' WHERE colid='RNSR';
UPDATE tlkptaxon SET newlabel='Castanea sativa Mill.', htmllabel='<i>Castanea sativa</i> Mill.', colparentid='62KM4' WHERE colid='5XCVW';
UPDATE tlkptaxon SET newlabel='Catalpa Scop.', htmllabel='<i>Catalpa</i> Scop.', colparentid='77P' WHERE colid='62KMN';
UPDATE tlkptaxon SET newlabel='Catalpa speciosa (Warder ex Barney) Warder ex Engelm.', htmllabel='<i>Catalpa speciosa</i> (Warder ex Barney) Warder ex Engelm.', colparentid='62KMN' WHERE colid='RRZ6';
UPDATE tlkptaxon SET newlabel='Ceanothus L.', htmllabel='<i>Ceanothus</i> L.', colparentid='KJF' WHERE colid='62KF2';
UPDATE tlkptaxon SET newlabel='Ceanothus crassifolius Torr.', htmllabel='<i>Ceanothus crassifolius</i> Torr.', colparentid='62KF2' WHERE colid='69H3P';
UPDATE tlkptaxon SET newlabel='Cedrela P. Browne', htmllabel='<i>Cedrela</i> P. Browne', colparentid='626TL' WHERE colid='3JX3';
UPDATE tlkptaxon SET newlabel='Cedrela odorata L.', htmllabel='<i>Cedrela odorata</i> L.', colparentid='3JX3' WHERE colid='RZZT';
UPDATE tlkptaxon SET newlabel='Cedrus', htmllabel='<i>Cedrus</i>', colparentid='625M7' WHERE colid='3JXB';
UPDATE tlkptaxon SET newlabel='Cedrus atlantica (Endl.) Manetti ex Carriere', htmllabel='<i>Cedrus atlantica</i> (Endl.) Manetti ex Carriere', colparentid='3JXB' WHERE colid='69GTZ';
UPDATE tlkptaxon SET newlabel='Cedrus deodara (Lamb.) G. Don', htmllabel='<i>Cedrus deodara</i> (Lamb.) G. Don', colparentid='3JXB' WHERE colid='S24D';
UPDATE tlkptaxon SET newlabel='Cedrus libani A. Rich.', htmllabel='<i>Cedrus libani</i> A. Rich.', colparentid='3JXB' WHERE colid='S24H';
UPDATE tlkptaxon SET newlabel='Celtis laevigata var. reticulata (Torr.) L. D. Benson', htmllabel='<i>Celtis laevigata</i> var. <i>reticulata</i> (Torr.) L. D. Benson', colparentid='S4RG' WHERE colid='5MY4C';
UPDATE tlkptaxon SET newlabel='Cercidium', htmllabel='<i>Cercidium</i>', colparentid='623QT' WHERE colid='3KZ2';
UPDATE tlkptaxon SET newlabel='Cercidium microphyllum (Torr.) Rose & I.M.Johnst.', htmllabel='<i>Cercidium microphyllum</i> (Torr.) Rose & I.M.Johnst.', colparentid='3KZ2' WHERE colid='SSK9';
UPDATE tlkptaxon SET newlabel='Cercocarpus Kunth', htmllabel='<i>Cercocarpus</i> Kunth', colparentid='JGW' WHERE colid='3KZG';
UPDATE tlkptaxon SET newlabel='Cercocarpus ledifolius Nutt.', htmllabel='<i>Cercocarpus ledifolius</i> Nutt.', colparentid='3KZG' WHERE colid='SSSJ';
UPDATE tlkptaxon SET newlabel='Cercocarpus montanus Raf.', htmllabel='<i>Cercocarpus montanus</i> Raf.', colparentid='3KZG' WHERE colid='SSSQ';
UPDATE tlkptaxon SET newlabel='Cercocarpus montanus var. glaber (S. Watson) F. L. Martin', htmllabel='<i>Cercocarpus montanus</i> var. <i>glaber</i> (S. Watson) F. L. Martin', colparentid='SSSQ' WHERE colid='5MZ2G';
UPDATE tlkptaxon SET newlabel='Chamaecyparis', htmllabel='<i>Chamaecyparis</i>', colparentid='8SY' WHERE colid='3LZ8';
UPDATE tlkptaxon SET newlabel='Chamaecyparis formosensis Matsum.', htmllabel='<i>Chamaecyparis formosensis</i> Matsum.', colparentid='3LZ8' WHERE colid='TJJ9';
UPDATE tlkptaxon SET newlabel='Chamaecyparis lawsoniana (A. Murray bis) Parl.', htmllabel='<i>Chamaecyparis lawsoniana</i> (A. Murray bis) Parl.', colparentid='3LZ8' WHERE colid='TJJD';
UPDATE tlkptaxon SET newlabel='Chamaecyparis pisifera (Siebold & Zucc.) Endl.', htmllabel='<i>Chamaecyparis pisifera</i> (Siebold & Zucc.) Endl.', colparentid='3LZ8' WHERE colid='TJJK';
UPDATE tlkptaxon SET newlabel='Chamaecyparis thyoides (L.) Britton, Sterns & Poggenb.', htmllabel='<i>Chamaecyparis thyoides</i> (L.) Britton, Sterns & Poggenb.', colparentid='3LZ8' WHERE colid='TJJP';
UPDATE tlkptaxon SET newlabel='Milicia Sim', htmllabel='<i>Milicia</i> Sim', colparentid='CXM' WHERE colid='5SSS';
UPDATE tlkptaxon SET newlabel='Citharexylum', htmllabel='<i>Citharexylum</i>', colparentid='HST' WHERE colid='3Q9V';
UPDATE tlkptaxon SET newlabel='Citharexylum spinosum L.', htmllabel='<i>Citharexylum spinosum</i> L.', colparentid='3Q9V' WHERE colid='VM58';
UPDATE tlkptaxon SET newlabel='Cornaceae', htmllabel='Cornaceae', colparentid='ZT' WHERE colid='8KL';
UPDATE tlkptaxon SET newlabel='Cornales', htmllabel='Cornales', colparentid='MG' WHERE colid='ZT';
UPDATE tlkptaxon SET newlabel='Cornus', htmllabel='<i>Cornus</i>', colparentid='8KL' WHERE colid='62MBV';
UPDATE tlkptaxon SET newlabel='Cornus florida L.', htmllabel='<i>Cornus florida</i> L.', colparentid='62MBV' WHERE colid='YGJT';
UPDATE tlkptaxon SET newlabel='Cornus sanguinea L.', htmllabel='<i>Cornus sanguinea</i> L.', colparentid='62MBV' WHERE colid='YGMF';
UPDATE tlkptaxon SET newlabel='Corylus', htmllabel='<i>Corylus</i>', colparentid='777' WHERE colid='62NTW';
UPDATE tlkptaxon SET newlabel='Corylus avellana L.', htmllabel='<i>Corylus avellana</i> L.', colparentid='62NTW' WHERE colid='YQ55';
UPDATE tlkptaxon SET newlabel='Corylus sieboldiana Blume', htmllabel='<i>Corylus sieboldiana</i> Blume', colparentid='62NTW' WHERE colid='YQ6W';
UPDATE tlkptaxon SET newlabel='Cotinus Mill.', htmllabel='<i>Cotinus</i> Mill.', colparentid='J56' WHERE colid='62NHK';
UPDATE tlkptaxon SET newlabel='Cryptomeria', htmllabel='<i>Cryptomeria</i>', colparentid='8SY' WHERE colid='3WN6';
UPDATE tlkptaxon SET newlabel='Cryptomeria japonica (Thunb. ex L. f.) D. Don', htmllabel='<i>Cryptomeria japonica</i> (Thunb. ex L. f.) D. Don', colparentid='3WN6' WHERE colid='ZY5W';
UPDATE tlkptaxon SET newlabel='Cupressus', htmllabel='<i>Cupressus</i>', colparentid='8SY' WHERE colid='3XFC';
UPDATE tlkptaxon SET newlabel='Cupressus arizonica Greene', htmllabel='<i>Cupressus arizonica</i> Greene', colparentid='3XFC' WHERE colid='32FVX';
UPDATE tlkptaxon SET newlabel='Cupressus dupreziana A. Camus', htmllabel='<i>Cupressus dupreziana</i> A. Camus', colparentid='3XFC' WHERE colid='32FWG';
UPDATE tlkptaxon SET newlabel='Cupressus torulosa D. Don', htmllabel='<i>Cupressus torulosa</i> D. Don', colparentid='3XFC' WHERE colid='32FY9';
UPDATE tlkptaxon SET newlabel='Cupressus torulosa var. gigantea (W. C. Cheng & L.K. Fu) Farjon', htmllabel='<i>Cupressus torulosa</i> var. <i>gigantea</i> (W. C. Cheng & L.K. Fu) Farjon', colparentid='32FY9' WHERE colid='5NDMK';
UPDATE tlkptaxon SET newlabel='Fagaceae', htmllabel='Fagaceae', colparentid='384' WHERE colid='623R7';
UPDATE tlkptaxon SET newlabel='Ilex opaca Sol. ex Aiton', htmllabel='<i>Ilex opaca</i> Sol. ex Aiton', colparentid='62WJ5' WHERE colid='3PFRR';
UPDATE tlkptaxon SET newlabel='Juglandaceae DC. ex Perleb', htmllabel='Juglandaceae DC. ex Perleb', colparentid='384' WHERE colid='BJB';
UPDATE tlkptaxon SET newlabel='Lamiales Bromhead', htmllabel='Lamiales Bromhead', colparentid='MG' WHERE colid='3F4';
UPDATE tlkptaxon SET newlabel='Meliaceae Juss.', htmllabel='Meliaceae Juss.', colparentid='3ZY' WHERE colid='CKB';
UPDATE tlkptaxon SET newlabel='Rhamnaceae Juss.', htmllabel='Rhamnaceae Juss.', colparentid='3Z6' WHERE colid='FN6';
UPDATE tlkptaxon SET newlabel='Rubiaceae', htmllabel='Rubiaceae', colparentid='39D' WHERE colid='6278P';
UPDATE tlkptaxon SET newlabel='Lamiales Bromhead', htmllabel='Lamiales Bromhead', colparentid='MG' WHERE colid='3F4';
UPDATE tlkptaxon SET newlabel='Ulmaceae Mirb.', htmllabel='Ulmaceae Mirb.', colparentid='3Z6' WHERE colid='HNB';
UPDATE tlkptaxon SET newlabel='Verbenaceae', htmllabel='Verbenaceae', colparentid='3F4' WHERE colid='HST';
UPDATE tlkptaxon SET newlabel='Xanthocyparis', htmllabel='<i>Xanthocyparis</i>', colparentid='8SY' WHERE colid='649GY';
UPDATE tlkptaxon SET newlabel='Corymbia K. D. Hill & L. A. S. Johnson', htmllabel='<i>Corymbia</i> K. D. Hill & L. A. S. Johnson', colparentid='JY4' WHERE colid='62NTZ';
UPDATE tlkptaxon SET newlabel='Cupressus arizonica var. glabra (Sudw.) Little', htmllabel='<i>Cupressus arizonica</i> var. <i>glabra</i> (Sudw.) Little', colparentid='32FVX' WHERE colid='5NDL4';
UPDATE tlkptaxon SET newlabel='Afrocanthium', htmllabel='<i>Afrocanthium</i>', colparentid='6278P' WHERE colid='Q4Y';
UPDATE tlkptaxon SET newlabel='Celastrales Link', htmllabel='Celastrales Link', colparentid='MG' WHERE colid='W8';
UPDATE tlkptaxon SET newlabel='Celtis laevigata Willd.', htmllabel='<i>Celtis laevigata</i> Willd.', colparentid='3K3G' WHERE colid='S4RG';
UPDATE tlkptaxon SET newlabel='Celtis occidentalis L.', htmllabel='<i>Celtis occidentalis</i> L.', colparentid='3K3G' WHERE colid='S4SV';
UPDATE tlkptaxon SET newlabel='Cupressus lusitanica Mill.', htmllabel='<i>Cupressus lusitanica</i> Mill.', colparentid='3XFC' WHERE colid='6BRGS';
UPDATE tlkptaxon SET newlabel='Cupressus sempervirens L.', htmllabel='<i>Cupressus sempervirens</i> L.', colparentid='3XFC' WHERE colid='32FXZ';
UPDATE tlkptaxon SET newlabel='Cyrilla L.', htmllabel='<i>Cyrilla</i> L.', colparentid='8WM' WHERE colid='63259';
UPDATE tlkptaxon SET newlabel='Cyrilla racemiflora L.', htmllabel='<i>Cyrilla racemiflora</i> L.', colparentid='63259' WHERE colid='33JMD';
UPDATE tlkptaxon SET newlabel='Cyrillaceae Lindl.', htmllabel='Cyrillaceae Lindl.', colparentid='625QY' WHERE colid='8WM';
UPDATE tlkptaxon SET newlabel='Dacrycarpus', htmllabel='<i>Dacrycarpus</i>', colparentid='627HD' WHERE colid='3Z9D';
UPDATE tlkptaxon SET newlabel='Dacrydium', htmllabel='<i>Dacrydium</i>', colparentid='627HD' WHERE colid='3Z9F';
UPDATE tlkptaxon SET newlabel='Dacrydium cupressinum Sol. ex G. Forst.', htmllabel='<i>Dacrydium cupressinum</i> Sol. ex G. Forst.', colparentid='3Z9F' WHERE colid='33T9W';
UPDATE tlkptaxon SET newlabel='Dicorynia', htmllabel='<i>Dicorynia</i>', colparentid='623QT' WHERE colid='44SG';
UPDATE tlkptaxon SET newlabel='Dicorynia guianensis Amshoff', htmllabel='<i>Dicorynia guianensis</i> Amshoff', colparentid='44SG' WHERE colid='35QMN';
UPDATE tlkptaxon SET newlabel='Diospyros', htmllabel='<i>Diospyros</i>', colparentid='9JQ' WHERE colid='45ZZ';
UPDATE tlkptaxon SET newlabel='Diospyros virginiana L.', htmllabel='<i>Diospyros virginiana</i> L.', colparentid='45ZZ' WHERE colid='6D8DM';
UPDATE tlkptaxon SET newlabel='Drimys Forster & G. Forst.', htmllabel='<i>Drimys</i> Forster & G. Forst.', colparentid='6274R' WHERE colid='48BX';
UPDATE tlkptaxon SET newlabel='Drimys winteri J. R. Forst.', htmllabel='<i>Drimys winteri</i> J. R. Forst.', colparentid='48BX' WHERE colid='6DKK8';
UPDATE tlkptaxon SET newlabel='Ephedra', htmllabel='<i>Ephedra</i>', colparentid='9S8' WHERE colid='4CSV';
UPDATE tlkptaxon SET newlabel='Ephedraceae', htmllabel='Ephedraceae', colparentid='36Z' WHERE colid='9S8';
UPDATE tlkptaxon SET newlabel='Ephedrales', htmllabel='Ephedrales', colparentid='BZ' WHERE colid='36Z';
UPDATE tlkptaxon SET newlabel='Eucalyptus L''Hér.', htmllabel='<i>Eucalyptus</i> L''Hér.', colparentid='JY4' WHERE colid='4F2P';
UPDATE tlkptaxon SET newlabel='Eucalyptus delegatensis F. Muell. ex R. T. Baker', htmllabel='<i>Eucalyptus delegatensis</i> F. Muell. ex R. T. Baker', colparentid='4F2P' WHERE colid='6GSMS';
UPDATE tlkptaxon SET newlabel='Eucalyptus globulus Labill.', htmllabel='<i>Eucalyptus globulus</i> Labill.', colparentid='4F2P' WHERE colid='3BPZ6';
UPDATE tlkptaxon SET newlabel='Eucalyptus marginata Sm.', htmllabel='<i>Eucalyptus marginata</i> Sm.', colparentid='4F2P' WHERE colid='3BQ6Z';
UPDATE tlkptaxon SET newlabel='Eucalyptus miniata A. Cunn. ex Schauer', htmllabel='<i>Eucalyptus miniata</i> A. Cunn. ex Schauer', colparentid='4F2P' WHERE colid='3BQ7Y';
UPDATE tlkptaxon SET newlabel='Eucalyptus oreades F. Muell. ex R. T. Baker', htmllabel='<i>Eucalyptus oreades</i> F. Muell. ex R. T. Baker', colparentid='4F2P' WHERE colid='6H4NQ';
UPDATE tlkptaxon SET newlabel='Eucalyptus stellulata Sieber ex DC.', htmllabel='<i>Eucalyptus stellulata</i> Sieber ex DC.', colparentid='4F2P' WHERE colid='3BQJJ';
UPDATE tlkptaxon SET newlabel='Euphorbiaceae', htmllabel='Euphorbiaceae', colparentid='MLP' WHERE colid='9Y7';
UPDATE tlkptaxon SET newlabel='Fagus', htmllabel='<i>Fagus</i>', colparentid='623R7' WHERE colid='4J87';
UPDATE tlkptaxon SET newlabel='Fagus grandifolia Ehrh.', htmllabel='<i>Fagus grandifolia</i> Ehrh.', colparentid='4J87' WHERE colid='3DSHT';
UPDATE tlkptaxon SET newlabel='Fagus orientalis Lipsky', htmllabel='<i>Fagus orientalis</i> Lipsky', colparentid='4J87' WHERE colid='3DSJN';
UPDATE tlkptaxon SET newlabel='Fagus sylvatica L.', htmllabel='<i>Fagus sylvatica</i> L.', colparentid='4J87' WHERE colid='3DSK5';
UPDATE tlkptaxon SET newlabel='Fitzroya', htmllabel='<i>Fitzroya</i>', colparentid='8SY' WHERE colid='4K9L';
UPDATE tlkptaxon SET newlabel='Fitzroya cupressoides (Molina) I. M. Johnst.', htmllabel='<i>Fitzroya cupressoides</i> (Molina) I. M. Johnst.', colparentid='4K9L' WHERE colid='6J6YS';
UPDATE tlkptaxon SET newlabel='Fraxinus', htmllabel='<i>Fraxinus</i>', colparentid='DK4' WHERE colid='4L2N';
UPDATE tlkptaxon SET newlabel='Fraxinus caroliniana Mill.', htmllabel='<i>Fraxinus caroliniana</i> Mill.', colparentid='4L2N' WHERE colid='6JMRD';
UPDATE tlkptaxon SET newlabel='Fraxinus nigra Marshall', htmllabel='<i>Fraxinus nigra</i> Marshall', colparentid='4L2N' WHERE colid='6JMHJ';
UPDATE tlkptaxon SET newlabel='Fraxinus velutina Torr.', htmllabel='<i>Fraxinus velutina</i> Torr.', colparentid='4L2N' WHERE colid='6JM32';
UPDATE tlkptaxon SET newlabel='Gleditsia', htmllabel='<i>Gleditsia</i>', colparentid='623QT' WHERE colid='4NVR';
UPDATE tlkptaxon SET newlabel='Gleditsia triacanthos L.', htmllabel='<i>Gleditsia triacanthos</i> L.', colparentid='4NVR' WHERE colid='6KH48';
UPDATE tlkptaxon SET newlabel='Gmelina', htmllabel='<i>Gmelina</i>', colparentid='BR9' WHERE colid='4PHG';
UPDATE tlkptaxon SET newlabel='Gmelina arborea Roxb. ex Sm.', htmllabel='<i>Gmelina arborea</i> Roxb. ex Sm.', colparentid='4PHG' WHERE colid='3GJJK';
UPDATE tlkptaxon SET newlabel='Gnetopsida', htmllabel='Gnetopsida', colparentid='TP' WHERE colid='BZ';
UPDATE tlkptaxon SET newlabel='Gordonia Ellis', htmllabel='<i>Gordonia</i> Ellis', colparentid='H5S' WHERE colid='4QD8';
UPDATE tlkptaxon SET newlabel='Gordonia lasianthus L. ex Ellis', htmllabel='<i>Gordonia lasianthus</i> L. ex Ellis', colparentid='4QD8' WHERE colid='3H2VN';
UPDATE tlkptaxon SET newlabel='Halocarpus', htmllabel='<i>Halocarpus</i>', colparentid='627HD' WHERE colid='4SVB';
UPDATE tlkptaxon SET newlabel='Halocarpus biformis (Hook.) Quinn', htmllabel='<i>Halocarpus biformis</i> (Hook.) Quinn', colparentid='4SVB' WHERE colid='6KZVB';
UPDATE tlkptaxon SET newlabel='Halocarpus kirkii (F. Muell. ex Parl.) Quinn', htmllabel='<i>Halocarpus kirkii</i> (F. Muell. ex Parl.) Quinn', colparentid='4SVB' WHERE colid='6KZVC';
UPDATE tlkptaxon SET newlabel='Hevea', htmllabel='<i>Hevea</i>', colparentid='9Y7' WHERE colid='4WWS';
UPDATE tlkptaxon SET newlabel='Hevea brasiliensis (Willd. ex A.Juss.) Müll.Arg.', htmllabel='<i>Hevea brasiliensis</i> (Willd. ex A.Juss.) Müll.Arg.', colparentid='4WWS' WHERE colid='6LTD3';
UPDATE tlkptaxon SET newlabel='Ilex L. L.', htmllabel='<i>Ilex</i> L. L.', colparentid='6LY' WHERE colid='62WJ5';
UPDATE tlkptaxon SET newlabel='Ilex aquifolium L.', htmllabel='<i>Ilex aquifolium</i> L.', colparentid='62WJ5' WHERE colid='6N77K';
UPDATE tlkptaxon SET newlabel='Ilex cassine L.', htmllabel='<i>Ilex cassine</i> L.', colparentid='62WJ5' WHERE colid='3PF9M';
UPDATE tlkptaxon SET newlabel='Ilex coriacea (Pursh) Chapm.', htmllabel='<i>Ilex coriacea</i> (Pursh) Chapm.', colparentid='62WJ5' WHERE colid='6MV99';
UPDATE tlkptaxon SET newlabel='Ilex glabra (L.) A. Gray', htmllabel='<i>Ilex glabra</i> (L.) A. Gray', colparentid='62WJ5' WHERE colid='3PFFX';
UPDATE tlkptaxon SET newlabel='Juglans L.', htmllabel='<i>Juglans</i> L.', colparentid='JRQ' WHERE colid='56VV';
UPDATE tlkptaxon SET newlabel='Juglans cinerea L.', htmllabel='<i>Juglans cinerea</i> L.', colparentid='56VV' WHERE colid='3QRSG';
UPDATE tlkptaxon SET newlabel='Juglans nigra L.', htmllabel='<i>Juglans nigra</i> L.', colparentid='56VV' WHERE colid='6NFM9';
UPDATE tlkptaxon SET newlabel='Juglans regia L.', htmllabel='<i>Juglans regia</i> L.', colparentid='56VV' WHERE colid='6NFN8';
UPDATE tlkptaxon SET newlabel='Juniperus', htmllabel='<i>Juniperus</i>', colparentid='8SY' WHERE colid='56YL';
UPDATE tlkptaxon SET newlabel='Juniperus communis L.', htmllabel='<i>Juniperus communis</i> L.', colparentid='56YL' WHERE colid='6NGH4';
UPDATE tlkptaxon SET newlabel='Juniperus drupacea Labill.', htmllabel='<i>Juniperus drupacea</i> Labill.', colparentid='56YL' WHERE colid='6NGFW';
UPDATE tlkptaxon SET newlabel='Juniperus excelsa M.-Bieb.', htmllabel='<i>Juniperus excelsa</i> M.-Bieb.', colparentid='56YL' WHERE colid='6NGHR';
UPDATE tlkptaxon SET newlabel='Juniperus excelsa subsp. polycarpos (K. Koch) Takht.', htmllabel='<i>Juniperus excelsa</i> subsp. <i>polycarpos</i> (K. Koch) Takht.', colparentid='6NGHR' WHERE colid='5J6LL';
UPDATE tlkptaxon SET newlabel='Juniperus foetidissima Willd.', htmllabel='<i>Juniperus foetidissima</i> Willd.', colparentid='56YL' WHERE colid='6NGHP';
UPDATE tlkptaxon SET newlabel='Juniperus monosperma (Engelm.) Sarg.', htmllabel='<i>Juniperus monosperma</i> (Engelm.) Sarg.', colparentid='56YL' WHERE colid='6NFZD';
UPDATE tlkptaxon SET newlabel='Juniperus osteosperma (Torr.) Little', htmllabel='<i>Juniperus osteosperma</i> (Torr.) Little', colparentid='56YL' WHERE colid='6NFYV';
UPDATE tlkptaxon SET newlabel='Juniperus oxycedrus L.', htmllabel='<i>Juniperus oxycedrus</i> L.', colparentid='56YL' WHERE colid='6NFYP';
UPDATE tlkptaxon SET newlabel='Juniperus pinchotii Sudw.', htmllabel='<i>Juniperus pinchotii</i> Sudw.', colparentid='56YL' WHERE colid='6NFYL';
UPDATE tlkptaxon SET newlabel='Juniperus przewalskii Kom.', htmllabel='<i>Juniperus przewalskii</i> Kom.', colparentid='56YL' WHERE colid='6NFZW';
UPDATE tlkptaxon SET newlabel='Juniperus pseudosabina Fisch. & C.A. Mey.', htmllabel='<i>Juniperus pseudosabina</i> Fisch. & C.A. Mey.', colparentid='56YL' WHERE colid='6NFZQ';
UPDATE tlkptaxon SET newlabel='Juniperus recurva Buch.-Ham. ex D. Don', htmllabel='<i>Juniperus recurva</i> Buch.-Ham. ex D. Don', colparentid='56YL' WHERE colid='6NFZN';
UPDATE tlkptaxon SET newlabel='Juniperus rigida Siebold & Zucc.', htmllabel='<i>Juniperus rigida</i> Siebold & Zucc.', colparentid='56YL' WHERE colid='6NGBR';
UPDATE tlkptaxon SET newlabel='Juniperus rigida subsp. conferta (Parl.) Kitam.', htmllabel='<i>Juniperus rigida</i> subsp. <i>conferta</i> (Parl.) Kitam.', colparentid='6NGBR' WHERE colid='5J6M3';
UPDATE tlkptaxon SET newlabel='Juniperus scopulorum Sarg.', htmllabel='<i>Juniperus scopulorum</i> Sarg.', colparentid='56YL' WHERE colid='6NGBF';
UPDATE tlkptaxon SET newlabel='Juniperus semiglobosa Regel', htmllabel='<i>Juniperus semiglobosa</i> Regel', colparentid='56YL' WHERE colid='6NGBB';
UPDATE tlkptaxon SET newlabel='Juniperus thurifera L.', htmllabel='<i>Juniperus thurifera</i> L.', colparentid='56YL' WHERE colid='6NHWF';
UPDATE tlkptaxon SET newlabel='Juniperus virginiana L.', htmllabel='<i>Juniperus virginiana</i> L.', colparentid='56YL' WHERE colid='6NHW6';
UPDATE tlkptaxon SET newlabel='Laburnum', htmllabel='<i>Laburnum</i>', colparentid='623QT' WHERE colid='59QH';
UPDATE tlkptaxon SET newlabel='Lagarostrobos', htmllabel='<i>Lagarostrobos</i>', colparentid='627HD' WHERE colid='62VP6';
UPDATE tlkptaxon SET newlabel='Lagarostrobos franklinii (Hook. f.) Quinn', htmllabel='<i>Lagarostrobos franklinii</i> (Hook. f.) Quinn', colparentid='62VP6' WHERE colid='6NT8V';
UPDATE tlkptaxon SET newlabel='Larix', htmllabel='<i>Larix</i>', colparentid='625M7' WHERE colid='639D7';
UPDATE tlkptaxon SET newlabel='Malpighiales', htmllabel='Malpighiales', colparentid='MG' WHERE colid='MLP';
UPDATE tlkptaxon SET newlabel='Manoao', htmllabel='<i>Manoao</i>', colparentid='627HD' WHERE colid='5KXJ';
UPDATE tlkptaxon SET newlabel='Manoao colensoi (Hook.) Molloy', htmllabel='<i>Manoao colensoi</i> (Hook.) Molloy', colparentid='5KXJ' WHERE colid='3XVQM';
UPDATE tlkptaxon SET newlabel='Myrtaceae Juss.', htmllabel='Myrtaceae Juss.', colparentid='3LY' WHERE colid='D48';
UPDATE tlkptaxon SET newlabel='Myrtales Juss. ex Bercht. & J. Presl', htmllabel='Myrtales Juss. ex Bercht. & J. Presl', colparentid='MG' WHERE colid='3LY';
UPDATE tlkptaxon SET newlabel='Oleaceae', htmllabel='Oleaceae', colparentid='3F4' WHERE colid='DK4';
UPDATE tlkptaxon SET newlabel='Podocarpaceae', htmllabel='Podocarpaceae', colparentid='623FD' WHERE colid='627HD';
UPDATE tlkptaxon SET newlabel='Pouteria', htmllabel='<i>Pouteria</i>', colparentid='FY4' WHERE colid='6V9R';
UPDATE tlkptaxon SET newlabel='Pouteria glomerata subsp. glomerata', htmllabel='<i>Pouteria glomerata</i> subsp. <i>glomerata</i>', colparentid='6W7FG' WHERE colid='5KMHN';
UPDATE tlkptaxon SET newlabel='Sapotaceae', htmllabel='Sapotaceae', colparentid='625QY' WHERE colid='FY4';
UPDATE tlkptaxon SET newlabel='Theaceae Mirb. ex Ker Gawl.', htmllabel='Theaceae Mirb. ex Ker Gawl.', colparentid='625QY' WHERE colid='H5S';
UPDATE tlkptaxon SET newlabel='Winteraceae R. Br. ex Lindl.', htmllabel='Winteraceae R. Br. ex Lindl.', colparentid='VC' WHERE colid='6274R';
UPDATE tlkptaxon SET newlabel='Hamamelidaceae R. Br.', htmllabel='Hamamelidaceae R. Br.', colparentid='424' WHERE colid='624Z2';
UPDATE tlkptaxon SET newlabel='Larix decidua Mill.', htmllabel='<i>Larix decidua</i> Mill.', colparentid='639D7' WHERE colid='6NYWF';
UPDATE tlkptaxon SET newlabel='Dipsacales Juss. ex Bercht. & J. Presl', htmllabel='Dipsacales Juss. ex Bercht. & J. Presl', colparentid='MG' WHERE colid='35C';
UPDATE tlkptaxon SET newlabel='Fraxinus americana L.', htmllabel='<i>Fraxinus americana</i> L.', colparentid='4L2N' WHERE colid='6JN3T';
UPDATE tlkptaxon SET newlabel='Fraxinus excelsior L.', htmllabel='<i>Fraxinus excelsior</i> L.', colparentid='4L2N' WHERE colid='6JMBM';
UPDATE tlkptaxon SET newlabel='Larix gmelinii (Rupr.) Kuzen.', htmllabel='<i>Larix gmelinii</i> (Rupr.) Kuzen.', colparentid='639D7' WHERE colid='6NZ8C';
UPDATE tlkptaxon SET newlabel='Larix gmelinii var. gmelinii', htmllabel='<i>Larix gmelinii</i> var. <i>gmelinii</i>', colparentid='6NZ8C' WHERE colid='5PNDP';
UPDATE tlkptaxon SET newlabel='Larix griffithii Hook. f.', htmllabel='<i>Larix griffithii</i> Hook. f.', colparentid='639D7' WHERE colid='6NZ8D';
UPDATE tlkptaxon SET newlabel='Larix griffithii var. griffithii', htmllabel='<i>Larix griffithii</i> var. <i>griffithii</i>', colparentid='6NZ8D' WHERE colid='5PNDX';
UPDATE tlkptaxon SET newlabel='Larix laricina (Du Roi) K. Koch', htmllabel='<i>Larix laricina</i> (Du Roi) K. Koch', colparentid='639D7' WHERE colid='6NYVX';
UPDATE tlkptaxon SET newlabel='Larix lyallii Parl.', htmllabel='<i>Larix lyallii</i> Parl.', colparentid='639D7' WHERE colid='6NYW8';
UPDATE tlkptaxon SET newlabel='Larix sibirica Ledeb.', htmllabel='<i>Larix sibirica</i> Ledeb.', colparentid='639D7' WHERE colid='6NZ9P';
UPDATE tlkptaxon SET newlabel='Lauraceae Juss.', htmllabel='Lauraceae Juss.', colparentid='3F8' WHERE colid='BTB';
UPDATE tlkptaxon SET newlabel='Laurales Juss. ex Bercht. & J. Presl', htmllabel='Laurales Juss. ex Bercht. & J. Presl', colparentid='MG' WHERE colid='3F8';
UPDATE tlkptaxon SET newlabel='Lecythis', htmllabel='<i>Lecythis</i>', colparentid='624WH' WHERE colid='5BR4';
UPDATE tlkptaxon SET newlabel='Lecythis corrugata Poit.', htmllabel='<i>Lecythis corrugata</i> Poit.', colparentid='5BR4' WHERE colid='3SW5J';
UPDATE tlkptaxon SET newlabel='Lepidothamnus', htmllabel='<i>Lepidothamnus</i>', colparentid='627HD' WHERE colid='5CLL';
UPDATE tlkptaxon SET newlabel='Libocedrus', htmllabel='<i>Libocedrus</i>', colparentid='8SY' WHERE colid='62WRV';
UPDATE tlkptaxon SET newlabel='Libocedrus bidwillii Hook. f.', htmllabel='<i>Libocedrus bidwillii</i> Hook. f.', colparentid='62WRV' WHERE colid='6PWDZ';
UPDATE tlkptaxon SET newlabel='Libocedrus plumosa (D. Don) Sarg.', htmllabel='<i>Libocedrus plumosa</i> (D. Don) Sarg.', colparentid='62WRV' WHERE colid='6PWDV';
UPDATE tlkptaxon SET newlabel='Ligustrum', htmllabel='<i>Ligustrum</i>', colparentid='DK4' WHERE colid='62X65';
UPDATE tlkptaxon SET newlabel='Ligustrum vulgare L.', htmllabel='<i>Ligustrum vulgare</i> L.', colparentid='62X65' WHERE colid='6PYTF';
UPDATE tlkptaxon SET newlabel='Liquidambar L.', htmllabel='<i>Liquidambar</i> L.', colparentid='68K' WHERE colid='5FBY';
UPDATE tlkptaxon SET newlabel='Liquidambar styraciflua L.', htmllabel='<i>Liquidambar styraciflua</i> L.', colparentid='5FBY' WHERE colid='6QJRL';
UPDATE tlkptaxon SET newlabel='Liriodendron', htmllabel='<i>Liriodendron</i>', colparentid='CC9' WHERE colid='5FCK';
UPDATE tlkptaxon SET newlabel='Liriodendron tulipifera L.', htmllabel='<i>Liriodendron tulipifera</i> L.', colparentid='5FCK' WHERE colid='3VCGS';
UPDATE tlkptaxon SET newlabel='Lonicera L.', htmllabel='<i>Lonicera</i> L.', colparentid='J9V' WHERE colid='5GFJ';
UPDATE tlkptaxon SET newlabel='Lonicera xylosteum L.', htmllabel='<i>Lonicera xylosteum</i> L.', colparentid='5GFJ' WHERE colid='3VZJR';
UPDATE tlkptaxon SET newlabel='Magnolia', htmllabel='<i>Magnolia</i>', colparentid='CC9' WHERE colid='5K5W';
UPDATE tlkptaxon SET newlabel='Magnolia grandiflora L.', htmllabel='<i>Magnolia grandiflora</i> L.', colparentid='5K5W' WHERE colid='3XHB5';
UPDATE tlkptaxon SET newlabel='Magnolia nilagirica (Zenker) Figlar', htmllabel='<i>Magnolia nilagirica</i> (Zenker) Figlar', colparentid='5K5W' WHERE colid='3XHF6';
UPDATE tlkptaxon SET newlabel='Magnolia virginiana L.', htmllabel='<i>Magnolia virginiana</i> L.', colparentid='5K5W' WHERE colid='3XHLF';
UPDATE tlkptaxon SET newlabel='Magnoliaceae', htmllabel='Magnoliaceae', colparentid='3HL' WHERE colid='CC9';
UPDATE tlkptaxon SET newlabel='Manilkara', htmllabel='<i>Manilkara</i>', colparentid='FY4' WHERE colid='5KWC';
UPDATE tlkptaxon SET newlabel='Manilkara bidentata (A.DC.) A.Chev.', htmllabel='<i>Manilkara bidentata</i> (A.DC.) A.Chev.', colparentid='5KWC' WHERE colid='6R6H5';
UPDATE tlkptaxon SET newlabel='Morella Lour.', htmllabel='<i>Morella</i> Lour.', colparentid='D3J' WHERE colid='5V62';
UPDATE tlkptaxon SET newlabel='Morella cerifera (L.) Small', htmllabel='<i>Morella cerifera</i> (L.) Small', colparentid='5V62' WHERE colid='44CWQ';
UPDATE tlkptaxon SET newlabel='Morus L.', htmllabel='<i>Morus</i> L.', colparentid='CXM' WHERE colid='5VCQ';
UPDATE tlkptaxon SET newlabel='Morus alba L.', htmllabel='<i>Morus alba</i> L.', colparentid='5VCQ' WHERE colid='44FPX';
UPDATE tlkptaxon SET newlabel='Myrica L.', htmllabel='<i>Myrica</i> L.', colparentid='D3J' WHERE colid='5WHT';
UPDATE tlkptaxon SET newlabel='Myrica gale L.', htmllabel='<i>Myrica gale</i> L.', colparentid='5WHT' WHERE colid='458DN';
UPDATE tlkptaxon SET newlabel='Myricaceae Rich. ex Kunth', htmllabel='Myricaceae Rich. ex Kunth', colparentid='384' WHERE colid='D3J';
UPDATE tlkptaxon SET newlabel='Nothofagus', htmllabel='<i>Nothofagus</i>', colparentid='626G3' WHERE colid='654M';
UPDATE tlkptaxon SET newlabel='Nothofagus alpina (Poepp. & Endl.) Oerst.', htmllabel='<i>Nothofagus alpina</i> (Poepp. & Endl.) Oerst.', colparentid='654M' WHERE colid='47SYX';
UPDATE tlkptaxon SET newlabel='Nothofagus antarctica (G.Forst.) Oerst.', htmllabel='<i>Nothofagus antarctica</i> (G.Forst.) Oerst.', colparentid='654M' WHERE colid='47SYY';
UPDATE tlkptaxon SET newlabel='Nothofagus betuloides (Mirb.) Oerst.', htmllabel='<i>Nothofagus betuloides</i> (Mirb.) Oerst.', colparentid='654M' WHERE colid='47SZ5';
UPDATE tlkptaxon SET newlabel='Nothofagus cunninghamii (Hook.) Oerst.', htmllabel='<i>Nothofagus cunninghamii</i> (Hook.) Oerst.', colparentid='654M' WHERE colid='6SB9B';
UPDATE tlkptaxon SET newlabel='Nothofagus fusca (Hook.f.) Oerst.', htmllabel='<i>Nothofagus fusca</i> (Hook.f.) Oerst.', colparentid='654M' WHERE colid='47SZR';
UPDATE tlkptaxon SET newlabel='Nothofagus gunnii (Hook.f.) Oerst.', htmllabel='<i>Nothofagus gunnii</i> (Hook.f.) Oerst.', colparentid='654M' WHERE colid='74B9B';
UPDATE tlkptaxon SET newlabel='Nothofagus menziesii (Hook.f.) Oerst.', htmllabel='<i>Nothofagus menziesii</i> (Hook.f.) Oerst.', colparentid='654M' WHERE colid='47SZY';
UPDATE tlkptaxon SET newlabel='Nothofagus nitida (Phil.) Krasser', htmllabel='<i>Nothofagus nitida</i> (Phil.) Krasser', colparentid='654M' WHERE colid='47T24';
UPDATE tlkptaxon SET newlabel='Nothofagus obliqua (Mirb.) Oerst.', htmllabel='<i>Nothofagus obliqua</i> (Mirb.) Oerst.', colparentid='654M' WHERE colid='47T26';
UPDATE tlkptaxon SET newlabel='Nothofagus pumilio (Poepp. & Endl.) Krasser', htmllabel='<i>Nothofagus pumilio</i> (Poepp. & Endl.) Krasser', colparentid='654M' WHERE colid='47T2C';
UPDATE tlkptaxon SET newlabel='Nothofagus solandri (Hook.f.) Oerst.', htmllabel='<i>Nothofagus solandri</i> (Hook.f.) Oerst.', colparentid='654M' WHERE colid='47T2H';
UPDATE tlkptaxon SET newlabel='Nyssa', htmllabel='<i>Nyssa</i>', colparentid='DGG' WHERE colid='665H';
UPDATE tlkptaxon SET newlabel='Nyssa sylvatica Marshall', htmllabel='<i>Nyssa sylvatica</i> Marshall', colparentid='665H' WHERE colid='487MQ';
UPDATE tlkptaxon SET newlabel='Nyssaceae', htmllabel='Nyssaceae', colparentid='ZT' WHERE colid='DGG';
UPDATE tlkptaxon SET newlabel='Ostrya', htmllabel='<i>Ostrya</i>', colparentid='777' WHERE colid='63H3Q';
UPDATE tlkptaxon SET newlabel='Oxydendrum DC.', htmllabel='<i>Oxydendrum</i> DC.', colparentid='KH2' WHERE colid='63HRW';
UPDATE tlkptaxon SET newlabel='Oxydendrum arboreum (L.) DC.', htmllabel='<i>Oxydendrum arboreum</i> (L.) DC.', colparentid='63HRW' WHERE colid='75FSZ';
UPDATE tlkptaxon SET newlabel='Parapiptadenia', htmllabel='<i>Parapiptadenia</i>', colparentid='623QT' WHERE colid='63LR7';
UPDATE tlkptaxon SET newlabel='Parapiptadenia rigida (Benth.) Brenan', htmllabel='<i>Parapiptadenia rigida</i> (Benth.) Brenan', colparentid='63LR7' WHERE colid='4D8TK';
UPDATE tlkptaxon SET newlabel='Parkia', htmllabel='<i>Parkia</i>', colparentid='623QT' WHERE colid='6HWN';
UPDATE tlkptaxon SET newlabel='Parkia discolor Benth.', htmllabel='<i>Parkia discolor</i> Benth.', colparentid='6HWN' WHERE colid='4DN4H';
UPDATE tlkptaxon SET newlabel='Paulownia Siebold & Zucc.', htmllabel='<i>Paulownia</i> Siebold & Zucc.', colparentid='626CS' WHERE colid='6JPP';
UPDATE tlkptaxon SET newlabel='Persea Mill.', htmllabel='<i>Persea</i> Mill.', colparentid='BTB' WHERE colid='63KZV';
UPDATE tlkptaxon SET newlabel='Persea borbonia (L.) Spreng.', htmllabel='<i>Persea borbonia</i> (L.) Spreng.', colparentid='63KZV' WHERE colid='4F98R';
UPDATE tlkptaxon SET newlabel='Phyllocladaceae', htmllabel='Phyllocladaceae', colparentid='623FD' WHERE colid='625KL';
UPDATE tlkptaxon SET newlabel='Phyllocladus', htmllabel='<i>Phyllocladus</i>', colparentid='625KL' WHERE colid='63MMR';
UPDATE tlkptaxon SET newlabel='Phyllocladus aspleniifolius (Labill.) Hook. f.', htmllabel='<i>Phyllocladus aspleniifolius</i> (Labill.) Hook. f.', colparentid='63MMR' WHERE colid='4H5YP';
UPDATE tlkptaxon SET newlabel='Phyllocladus trichomanoides D. Don', htmllabel='<i>Phyllocladus trichomanoides</i> D. Don', colparentid='63MMR' WHERE colid='4H5YZ';
UPDATE tlkptaxon SET newlabel='Phyllocladus trichomanoides var. alpinus (Hook. f.) Parl.', htmllabel='<i>Phyllocladus trichomanoides</i> var. <i>alpinus</i> (Hook. f.) Parl.', colparentid='4H5YZ' WHERE colid='7MF94';
UPDATE tlkptaxon SET newlabel='Picea', htmllabel='<i>Picea</i>', colparentid='625M7' WHERE colid='6Q7C';
UPDATE tlkptaxon SET newlabel='Picea abies (L.) H. Karst.', htmllabel='<i>Picea abies</i> (L.) H. Karst.', colparentid='6Q7C' WHERE colid='4HPZF';
UPDATE tlkptaxon SET newlabel='Picea asperata Mast.', htmllabel='<i>Picea asperata</i> Mast.', colparentid='6Q7C' WHERE colid='4HPZT';
UPDATE tlkptaxon SET newlabel='Picea brachytyla (Franch.) E. Pritz.', htmllabel='<i>Picea brachytyla</i> (Franch.) E. Pritz.', colparentid='6Q7C' WHERE colid='4HQ23';
UPDATE tlkptaxon SET newlabel='Picea chihuahuana Martínez', htmllabel='<i>Picea chihuahuana</i> Martínez', colparentid='6Q7C' WHERE colid='6VJ27';
UPDATE tlkptaxon SET newlabel='Picea engelmannii Parry ex Engelm.', htmllabel='<i>Picea engelmannii</i> Parry ex Engelm.', colparentid='6Q7C' WHERE colid='4HQ2J';
UPDATE tlkptaxon SET newlabel='Picea glauca (Moench) Voss', htmllabel='<i>Picea glauca</i> (Moench) Voss', colparentid='6Q7C' WHERE colid='4HQ2V';
UPDATE tlkptaxon SET newlabel='Picea glehnii (F. Schmidt) Mast.', htmllabel='<i>Picea glehnii</i> (F. Schmidt) Mast.', colparentid='6Q7C' WHERE colid='4HQ2X';
UPDATE tlkptaxon SET newlabel='Picea likiangensis (Franch.) E. Pritz.', htmllabel='<i>Picea likiangensis</i> (Franch.) E. Pritz.', colparentid='6Q7C' WHERE colid='6VJ29';
UPDATE tlkptaxon SET newlabel='Picea likiangensis var. rubescens Rehd. & E.H. Wilson', htmllabel='<i>Picea likiangensis</i> var. <i>rubescens</i> Rehd. & E.H. Wilson', colparentid='6VJ29' WHERE colid='7MF6S';
UPDATE tlkptaxon SET newlabel='Picea mariana (Mill.) Britton & et al.', htmllabel='<i>Picea mariana</i> (Mill.) Britton & et al.', colparentid='6Q7C' WHERE colid='4HQ3K';
UPDATE tlkptaxon SET newlabel='Picea obovata Ledeb.', htmllabel='<i>Picea obovata</i> Ledeb.', colparentid='6Q7C' WHERE colid='4HQ46';
UPDATE tlkptaxon SET newlabel='Picea omorika (Pancic) Purk.', htmllabel='<i>Picea omorika</i> (Pancic) Purk.', colparentid='6Q7C' WHERE colid='4HQ47';
UPDATE tlkptaxon SET newlabel='Picea orientalis (L.) Peterm.', htmllabel='<i>Picea orientalis</i> (L.) Peterm.', colparentid='6Q7C' WHERE colid='4HQ48';
UPDATE tlkptaxon SET newlabel='Picea pungens Engelm.', htmllabel='<i>Picea pungens</i> Engelm.', colparentid='6Q7C' WHERE colid='4HQ4J';
UPDATE tlkptaxon SET newlabel='Picea purpurea Mast.', htmllabel='<i>Picea purpurea</i> Mast.', colparentid='6Q7C' WHERE colid='4HQ4L';
UPDATE tlkptaxon SET newlabel='Picea rubens Sarg.', htmllabel='<i>Picea rubens</i> Sarg.', colparentid='6Q7C' WHERE colid='4HQ4Q';
UPDATE tlkptaxon SET newlabel='Picea schrenkiana Fisch. & C.A. Mey.', htmllabel='<i>Picea schrenkiana</i> Fisch. & C.A. Mey.', colparentid='6Q7C' WHERE colid='6VJ2C';
UPDATE tlkptaxon SET newlabel='Picea smithiana (Wall.) Boiss.', htmllabel='<i>Picea smithiana</i> (Wall.) Boiss.', colparentid='6Q7C' WHERE colid='4HQ52';
UPDATE tlkptaxon SET newlabel='Pilgerodendron', htmllabel='<i>Pilgerodendron</i>', colparentid='8SY' WHERE colid='6QF5';
UPDATE tlkptaxon SET newlabel='Pilgerodendron uviferum (D. Don) Florin', htmllabel='<i>Pilgerodendron uviferum</i> (D. Don) Florin', colparentid='6QF5' WHERE colid='4HTLM';
UPDATE tlkptaxon SET newlabel='Pinus', htmllabel='<i>Pinus</i>', colparentid='625M7' WHERE colid='6QPY';
UPDATE tlkptaxon SET newlabel='Pinus albicaulis Engelm.', htmllabel='<i>Pinus albicaulis</i> Engelm.', colparentid='6QPY' WHERE colid='4J224';
UPDATE tlkptaxon SET newlabel='Calceolariaceae Olmstead', htmllabel='Calceolariaceae Olmstead', colparentid='3F4' WHERE colid='7JJ';
UPDATE tlkptaxon SET newlabel='Malvaceae Juss.', htmllabel='Malvaceae Juss.', colparentid='3HP' WHERE colid='CDB';
UPDATE tlkptaxon SET newlabel='Pinus armandii Franch.', htmllabel='<i>Pinus armandii</i> Franch.', colparentid='6QPY' WHERE colid='4J22Q';
UPDATE tlkptaxon SET newlabel='Afrocarpus', htmllabel='<i>Afrocarpus</i>', colparentid='627HD' WHERE colid='Q53';
UPDATE tlkptaxon SET newlabel='Aquifoliaceae Bercht. & J. Presl', htmllabel='Aquifoliaceae Bercht. & J. Presl', colparentid='QG' WHERE colid='6LY';
UPDATE tlkptaxon SET newlabel='Caryophyllales Juss. ex Bercht. & J. Presl', htmllabel='Caryophyllales Juss. ex Bercht. & J. Presl', colparentid='MG' WHERE colid='VW';
UPDATE tlkptaxon SET newlabel='Clusiaceae Lindl.', htmllabel='Clusiaceae Lindl.', colparentid='MLP' WHERE colid='8CK';
UPDATE tlkptaxon SET newlabel='Morus rubra L.', htmllabel='<i>Morus rubra</i> L.', colparentid='5VCQ' WHERE colid='73R6J';
UPDATE tlkptaxon SET newlabel='Nyctaginaceae Juss.', htmllabel='Nyctaginaceae Juss.', colparentid='VW' WHERE colid='DG4';
UPDATE tlkptaxon SET newlabel='Pinus balfouriana Balf.', htmllabel='<i>Pinus balfouriana</i> Balf.', colparentid='6QPY' WHERE colid='4J234';
UPDATE tlkptaxon SET newlabel='Pinus banksiana Lamb.', htmllabel='<i>Pinus banksiana</i> Lamb.', colparentid='6QPY' WHERE colid='4J237';
UPDATE tlkptaxon SET newlabel='Pinus brutia Ten.', htmllabel='<i>Pinus brutia</i> Ten.', colparentid='6QPY' WHERE colid='77KTL';
UPDATE tlkptaxon SET newlabel='Pinus bungeana Zucc. ex Endl.', htmllabel='<i>Pinus bungeana</i> Zucc. ex Endl.', colparentid='6QPY' WHERE colid='4J23W';
UPDATE tlkptaxon SET newlabel='Pinus canariensis C. Sm.', htmllabel='<i>Pinus canariensis</i> C. Sm.', colparentid='6QPY' WHERE colid='4J245';
UPDATE tlkptaxon SET newlabel='Pinus contorta Douglas ex Loudon', htmllabel='<i>Pinus contorta</i> Douglas ex Loudon', colparentid='6QPY' WHERE colid='4J24Y';
UPDATE tlkptaxon SET newlabel='Pinus coulteri D. Don', htmllabel='<i>Pinus coulteri</i> D. Don', colparentid='6QPY' WHERE colid='4J254';
UPDATE tlkptaxon SET newlabel='Pinus densata Mast.', htmllabel='<i>Pinus densata</i> Mast.', colparentid='6QPY' WHERE colid='4J25L';
UPDATE tlkptaxon SET newlabel='Pinus densiflora Siebold & Zucc.', htmllabel='<i>Pinus densiflora</i> Siebold & Zucc.', colparentid='6QPY' WHERE colid='4J25P';
UPDATE tlkptaxon SET newlabel='Pinus echinata Mill.', htmllabel='<i>Pinus echinata</i> Mill.', colparentid='6QPY' WHERE colid='4J267';
UPDATE tlkptaxon SET newlabel='Pinus edulis Engelm.', htmllabel='<i>Pinus edulis</i> Engelm.', colparentid='6QPY' WHERE colid='4J269';
UPDATE tlkptaxon SET newlabel='Pinus elliottii Engelm.', htmllabel='<i>Pinus elliottii</i> Engelm.', colparentid='6QPY' WHERE colid='77KTN';
UPDATE tlkptaxon SET newlabel='Pinus flexilis E. James', htmllabel='<i>Pinus flexilis</i> E. James', colparentid='6QPY' WHERE colid='4J273';
UPDATE tlkptaxon SET newlabel='Pinus gerardiana Wall. ex D. Don', htmllabel='<i>Pinus gerardiana</i> Wall. ex D. Don', colparentid='6QPY' WHERE colid='6VL5M';
UPDATE tlkptaxon SET newlabel='Pinus halepensis Mill.', htmllabel='<i>Pinus halepensis</i> Mill.', colparentid='6QPY' WHERE colid='4J27Y';
UPDATE tlkptaxon SET newlabel='Pinus hartwegii Lindl.', htmllabel='<i>Pinus hartwegii</i> Lindl.', colparentid='6QPY' WHERE colid='4J285';
UPDATE tlkptaxon SET newlabel='Pinus heldreichii H. Christ', htmllabel='<i>Pinus heldreichii</i> H. Christ', colparentid='6QPY' WHERE colid='4J287';
UPDATE tlkptaxon SET newlabel='Pinus jeffreyi Balf.', htmllabel='<i>Pinus jeffreyi</i> Balf.', colparentid='6QPY' WHERE colid='77KTP';
UPDATE tlkptaxon SET newlabel='Pinus kesiya Royle ex Gordon', htmllabel='<i>Pinus kesiya</i> Royle ex Gordon', colparentid='6QPY' WHERE colid='4J29G';
UPDATE tlkptaxon SET newlabel='Pinus koraiensis Siebold & Zucc.', htmllabel='<i>Pinus koraiensis</i> Siebold & Zucc.', colparentid='6QPY' WHERE colid='4J29P';
UPDATE tlkptaxon SET newlabel='Pinus lambertiana Douglas', htmllabel='<i>Pinus lambertiana</i> Douglas', colparentid='6QPY' WHERE colid='4J29V';
UPDATE tlkptaxon SET newlabel='Pinus leiophylla Schiede ex Schltdl. & Cham.', htmllabel='<i>Pinus leiophylla</i> Schiede ex Schltdl. & Cham.', colparentid='6QPY' WHERE colid='6VL5T';
UPDATE tlkptaxon SET newlabel='Pinus longaeva D.K. Bailey', htmllabel='<i>Pinus longaeva</i> D.K. Bailey', colparentid='6QPY' WHERE colid='77L64';
UPDATE tlkptaxon SET newlabel='Pinus merkusii Jungh. & de Vriese', htmllabel='<i>Pinus merkusii</i> Jungh. & de Vriese', colparentid='6QPY' WHERE colid='4J2BY';
UPDATE tlkptaxon SET newlabel='Pinus monticola Douglas ex D. Don', htmllabel='<i>Pinus monticola</i> Douglas ex D. Don', colparentid='6QPY' WHERE colid='4J2CG';
UPDATE tlkptaxon SET newlabel='Pinus uncinata Ramond ex DC.', htmllabel='<i>Pinus uncinata</i> Ramond ex DC.', colparentid='6QPY' WHERE colid='4J2KB';
UPDATE tlkptaxon SET newlabel='Pinus mugo Turra', htmllabel='<i>Pinus mugo</i> Turra', colparentid='6QPY' WHERE colid='4J2CJ';
UPDATE tlkptaxon SET newlabel='Pinus nigra subsp. pallasiana (Lamb.) Holmboe', htmllabel='<i>Pinus nigra</i> subsp. <i>pallasiana</i> (Lamb.) Holmboe', colparentid='4J2D5' WHERE colid='7KLV8';
UPDATE tlkptaxon SET newlabel='Pinus oocarpa Schiede ex Schltdl.', htmllabel='<i>Pinus oocarpa</i> Schiede ex Schltdl.', colparentid='6QPY' WHERE colid='4J2DP';
UPDATE tlkptaxon SET newlabel='Pinus palustris Mill.', htmllabel='<i>Pinus palustris</i> Mill.', colparentid='6QPY' WHERE colid='4J2DX';
UPDATE tlkptaxon SET newlabel='Pinus peuce Griseb.', htmllabel='<i>Pinus peuce</i> Griseb.', colparentid='6QPY' WHERE colid='77L6F';
UPDATE tlkptaxon SET newlabel='Pinus pinaster Aiton', htmllabel='<i>Pinus pinaster</i> Aiton', colparentid='6QPY' WHERE colid='77KSH';
UPDATE tlkptaxon SET newlabel='Pinus pinea L.', htmllabel='<i>Pinus pinea</i> L.', colparentid='6QPY' WHERE colid='77KSK';
UPDATE tlkptaxon SET newlabel='Pinus ponderosa Douglas ex C. Lawson', htmllabel='<i>Pinus ponderosa</i> Douglas ex C. Lawson', colparentid='6QPY' WHERE colid='4J2F3';
UPDATE tlkptaxon SET newlabel='Pinus pumila (Pall.) Regel', htmllabel='<i>Pinus pumila</i> (Pall.) Regel', colparentid='6QPY' WHERE colid='4J2FH';
UPDATE tlkptaxon SET newlabel='Pinus pungens Lamb.', htmllabel='<i>Pinus pungens</i> Lamb.', colparentid='6QPY' WHERE colid='4J2FK';
UPDATE tlkptaxon SET newlabel='Pinus radiata D. Don', htmllabel='<i>Pinus radiata</i> D. Don', colparentid='6QPY' WHERE colid='4J2FP';
UPDATE tlkptaxon SET newlabel='Pinus roxburghii Sarg.', htmllabel='<i>Pinus roxburghii</i> Sarg.', colparentid='6QPY' WHERE colid='4J2GF';
UPDATE tlkptaxon SET newlabel='Pinus strobiformis Engelm.', htmllabel='<i>Pinus strobiformis</i> Engelm.', colparentid='6QPY' WHERE colid='4J2HX';
UPDATE tlkptaxon SET newlabel='Pinus sylvestris L.', htmllabel='<i>Pinus sylvestris</i> L.', colparentid='6QPY' WHERE colid='4J2J5';
UPDATE tlkptaxon SET newlabel='Pinus thunbergii Parl.', htmllabel='<i>Pinus thunbergii</i> Parl.', colparentid='6QPY' WHERE colid='4J2JV';
UPDATE tlkptaxon SET newlabel='Pinus torreyana Parry ex Carrière', htmllabel='<i>Pinus torreyana</i> Parry ex Carrière', colparentid='6QPY' WHERE colid='4J2JZ';
UPDATE tlkptaxon SET newlabel='Pinus virginiana Mill.', htmllabel='<i>Pinus virginiana</i> Mill.', colparentid='6QPY' WHERE colid='4J2KR';
UPDATE tlkptaxon SET newlabel='Pisonia L.', htmllabel='<i>Pisonia</i> L.', colparentid='DG4' WHERE colid='6QWP';
UPDATE tlkptaxon SET newlabel='Pisonia grandis R. Br.', htmllabel='<i>Pisonia grandis</i> R. Br.', colparentid='6QWP' WHERE colid='6VMGL';
UPDATE tlkptaxon SET newlabel='Pistacia L.', htmllabel='<i>Pistacia</i> L.', colparentid='J56' WHERE colid='6QX7';
UPDATE tlkptaxon SET newlabel='Pistacia atlantica Desf.', htmllabel='<i>Pistacia atlantica</i> Desf.', colparentid='6QX7' WHERE colid='4J9TB';
UPDATE tlkptaxon SET newlabel='Pistacia vera L.', htmllabel='<i>Pistacia vera</i> L.', colparentid='6QX7' WHERE colid='77MJ8';
UPDATE tlkptaxon SET newlabel='Platanaceae T. Lestib.', htmllabel='Platanaceae T. Lestib.', colparentid='3WP' WHERE colid='6262P';
UPDATE tlkptaxon SET newlabel='Platanus L.', htmllabel='<i>Platanus</i> L.', colparentid='6262P' WHERE colid='6RKR';
UPDATE tlkptaxon SET newlabel='Platanus occidentalis L.', htmllabel='<i>Platanus occidentalis</i> L.', colparentid='6RKR' WHERE colid='4JN3N';
UPDATE tlkptaxon SET newlabel='Platonia Mart.', htmllabel='<i>Platonia</i> Mart.', colparentid='JC8' WHERE colid='6RML';
UPDATE tlkptaxon SET newlabel='Podocarpus', htmllabel='<i>Podocarpus</i>', colparentid='627HD' WHERE colid='6TC9';
UPDATE tlkptaxon SET newlabel='Podocarpus cunninghamii Colenso', htmllabel='<i>Podocarpus cunninghamii</i> Colenso', colparentid='6TC9' WHERE colid='4KQ24';
UPDATE tlkptaxon SET newlabel='Podocarpus lawrencei Hook. f.', htmllabel='<i>Podocarpus lawrencei</i> Hook. f.', colparentid='6TC9' WHERE colid='6VQWN';
UPDATE tlkptaxon SET newlabel='Podocarpus neriifolius D.Don', htmllabel='<i>Podocarpus neriifolius</i> D.Don', colparentid='6TC9' WHERE colid='77QWN';
UPDATE tlkptaxon SET newlabel='Podocarpus nivalis Hook.', htmllabel='<i>Podocarpus nivalis</i> Hook.', colparentid='6TC9' WHERE colid='4KQ5F';
UPDATE tlkptaxon SET newlabel='Podocarpus parlatorei Pilg.', htmllabel='<i>Podocarpus parlatorei</i> Pilg.', colparentid='6TC9' WHERE colid='4KQ5T';
UPDATE tlkptaxon SET newlabel='Podocarpus totara G. Benn. ex D. Don', htmllabel='<i>Podocarpus totara</i> G. Benn. ex D. Don', colparentid='6TC9' WHERE colid='4KQ7J';
UPDATE tlkptaxon SET newlabel='Populus L.', htmllabel='<i>Populus</i> L.', colparentid='628MQ' WHERE colid='63PVP';
UPDATE tlkptaxon SET newlabel='Populus alba L.', htmllabel='<i>Populus alba</i> L.', colparentid='63PVP' WHERE colid='4LVJ5';
UPDATE tlkptaxon SET newlabel='Populus angustifolia James', htmllabel='<i>Populus angustifolia</i> James', colparentid='63PVP' WHERE colid='77VW4';
UPDATE tlkptaxon SET newlabel='Populus balsamifera L.', htmllabel='<i>Populus balsamifera</i> L.', colparentid='63PVP' WHERE colid='4LVJS';
UPDATE tlkptaxon SET newlabel='Populus deltoides W. Bartram ex Marshall', htmllabel='<i>Populus deltoides</i> W. Bartram ex Marshall', colparentid='63PVP' WHERE colid='4LVKV';
UPDATE tlkptaxon SET newlabel='Populus fremontii S. Watson', htmllabel='<i>Populus fremontii</i> S. Watson', colparentid='63PVP' WHERE colid='6W7W3';
UPDATE tlkptaxon SET newlabel='Populus grandidentata Michx.', htmllabel='<i>Populus grandidentata</i> Michx.', colparentid='63PVP' WHERE colid='4LVLR';
UPDATE tlkptaxon SET newlabel='Populus nigra L.', htmllabel='<i>Populus nigra</i> L.', colparentid='63PVP' WHERE colid='4LVNQ';
UPDATE tlkptaxon SET newlabel='Populus tremuloides Michx.', htmllabel='<i>Populus tremuloides</i> Michx.', colparentid='63PVP' WHERE colid='4LVR2';
UPDATE tlkptaxon SET newlabel='Prosopis', htmllabel='<i>Prosopis</i>', colparentid='623QT' WHERE colid='6X78';
UPDATE tlkptaxon SET newlabel='Prosopis flexuosa DC.', htmllabel='<i>Prosopis flexuosa</i> DC.', colparentid='6X78' WHERE colid='4MW6R';
UPDATE tlkptaxon SET newlabel='Prosopis glandulosa Torr.', htmllabel='<i>Prosopis glandulosa</i> Torr.', colparentid='6X78' WHERE colid='789K8';
UPDATE tlkptaxon SET newlabel='Prosopis glandulosa var. glandulosa', htmllabel='<i>Prosopis glandulosa</i> var. <i>glandulosa</i>', colparentid='789K8' WHERE colid='7MHHZ';
UPDATE tlkptaxon SET newlabel='Prumnopitys', htmllabel='<i>Prumnopitys</i>', colparentid='627HD' WHERE colid='6Y68';
UPDATE tlkptaxon SET newlabel='Prumnopitys taxifolia (Banks & Sol. ex D. Don) de Laub.', htmllabel='<i>Prumnopitys taxifolia</i> (Banks & Sol. ex D. Don) de Laub.', colparentid='6Y68' WHERE colid='6VYM8';
UPDATE tlkptaxon SET newlabel='Prunus L.', htmllabel='<i>Prunus</i> L.', colparentid='JV6' WHERE colid='6Y6H';
UPDATE tlkptaxon SET newlabel='Prunus mahaleb L.', htmllabel='<i>Prunus mahaleb</i> L.', colparentid='6Y6H' WHERE colid='77ZQW';
UPDATE tlkptaxon SET newlabel='Prunus pensylvanica L. fil.', htmllabel='<i>Prunus pensylvanica</i> L. fil.', colparentid='6Y6H' WHERE colid='77ZRD';
UPDATE tlkptaxon SET newlabel='Pseudotsuga', htmllabel='<i>Pseudotsuga</i>', colparentid='625M7' WHERE colid='73Y7';
UPDATE tlkptaxon SET newlabel='Pseudotsuga japonica (Shiras.) Beissn.', htmllabel='<i>Pseudotsuga japonica</i> (Shiras.) Beissn.', colparentid='73Y7' WHERE colid='78HCK';
UPDATE tlkptaxon SET newlabel='Pseudotsuga macrocarpa (Vasey) Mayr', htmllabel='<i>Pseudotsuga macrocarpa</i> (Vasey) Mayr', colparentid='73Y7' WHERE colid='78HCJ';
UPDATE tlkptaxon SET newlabel='Pseudotsuga menziesii (Mirb.) Franco', htmllabel='<i>Pseudotsuga menziesii</i> (Mirb.) Franco', colparentid='73Y7' WHERE colid='6WHCL';
UPDATE tlkptaxon SET newlabel='Pseudoxandra', htmllabel='<i>Pseudoxandra</i>', colparentid='6FB' WHERE colid='742N';
UPDATE tlkptaxon SET newlabel='Pseudoxandra polyphleba (Diels) R.E.Fr.', htmllabel='<i>Pseudoxandra polyphleba</i> (Diels) R.E.Fr.', colparentid='742N' WHERE colid='6WJ73';
UPDATE tlkptaxon SET newlabel='Pterocarpus', htmllabel='<i>Pterocarpus</i>', colparentid='623QT' WHERE colid='74N7';
UPDATE tlkptaxon SET newlabel='Pterocarpus angolensis DC.', htmllabel='<i>Pterocarpus angolensis</i> DC.', colparentid='74N7' WHERE colid='4PVGN';
UPDATE tlkptaxon SET newlabel='Purshia DC.', htmllabel='<i>Purshia</i> DC.', colparentid='JGW' WHERE colid='75K9';
UPDATE tlkptaxon SET newlabel='Quercus kelloggii Newb.', htmllabel='<i>Quercus kelloggii</i> Newb.', colparentid='76HN' WHERE colid='78RPJ';
UPDATE tlkptaxon SET newlabel='Salicaceae Mirb.', htmllabel='Salicaceae Mirb.', colparentid='MLP' WHERE colid='FWG';
UPDATE tlkptaxon SET newlabel='Purshia tridentata (Pursh) DC.', htmllabel='<i>Purshia tridentata</i> (Pursh) DC.', colparentid='75K9' WHERE colid='4QKRX';
UPDATE tlkptaxon SET newlabel='Caprifoliaceae Juss.', htmllabel='Caprifoliaceae Juss.', colparentid='35C' WHERE colid='7PP';
UPDATE tlkptaxon SET newlabel='Frangula Mill.', htmllabel='<i>Frangula</i> Mill.', colparentid='K9G' WHERE colid='4KZF';
UPDATE tlkptaxon SET newlabel='Juniperus indica Bertol.', htmllabel='<i>Juniperus indica</i> Bertol.', colparentid='56YL' WHERE colid='6NGHZ';
UPDATE tlkptaxon SET newlabel='Juniperus indica var. indica', htmllabel='<i>Juniperus indica</i> var. <i>indica</i>', colparentid='6NGHZ' WHERE colid='5PKXC';
UPDATE tlkptaxon SET newlabel='Juniperus recurva var. recurva', htmllabel='<i>Juniperus recurva</i> var. <i>recurva</i>', colparentid='6NFZN' WHERE colid='5PKYQ';
UPDATE tlkptaxon SET newlabel='Juniperus saltuaria Rehd. & E.H. Wilson', htmllabel='<i>Juniperus saltuaria</i> Rehd. & E.H. Wilson', colparentid='56YL' WHERE colid='6NGBG';
UPDATE tlkptaxon SET newlabel='Juniperus tibetica Kom.', htmllabel='<i>Juniperus tibetica</i> Kom.', colparentid='56YL' WHERE colid='6NHVZ';
UPDATE tlkptaxon SET newlabel='Primulaceae Batsch ex Borkh.', htmllabel='Primulaceae Batsch ex Borkh.', colparentid='625QY' WHERE colid='627BN';
UPDATE tlkptaxon SET newlabel='Myrsine L.', htmllabel='<i>Myrsine</i> L.', colparentid='JY3' WHERE colid='5WR8';
UPDATE tlkptaxon SET newlabel='Myrsine floridana A. DC.', htmllabel='<i>Myrsine floridana</i> A. DC.', colparentid='5WR8' WHERE colid='73SY5';
UPDATE tlkptaxon SET newlabel='Pinus nigra J.F. Arnold', htmllabel='<i>Pinus nigra</i> J.F. Arnold', colparentid='6QPY' WHERE colid='4J2D5';
UPDATE tlkptaxon SET newlabel='Platycladus', htmllabel='<i>Platycladus</i>', colparentid='8SY' WHERE colid='6RQZ';
UPDATE tlkptaxon SET newlabel='Quercus', htmllabel='<i>Quercus</i>', colparentid='623R7' WHERE colid='76HN';
UPDATE tlkptaxon SET newlabel='Quercus afares Pomel', htmllabel='<i>Quercus afares</i> Pomel', colparentid='76HN' WHERE colid='4R47F';
UPDATE tlkptaxon SET newlabel='Quercus alba L.', htmllabel='<i>Quercus alba</i> L.', colparentid='76HN' WHERE colid='4R47Z';
UPDATE tlkptaxon SET newlabel='Quercus brantii Lindl.', htmllabel='<i>Quercus brantii</i> Lindl.', colparentid='76HN' WHERE colid='4R4FG';
UPDATE tlkptaxon SET newlabel='Quercus canariensis Willd.', htmllabel='<i>Quercus canariensis</i> Willd.', colparentid='76HN' WHERE colid='4R4HG';
UPDATE tlkptaxon SET newlabel='Quercus coccifera L.', htmllabel='<i>Quercus coccifera</i> L.', colparentid='76HN' WHERE colid='4R4M7';
UPDATE tlkptaxon SET newlabel='Quercus costaricensis Liebm.', htmllabel='<i>Quercus costaricensis</i> Liebm.', colparentid='76HN' WHERE colid='793R5';
UPDATE tlkptaxon SET newlabel='Quercus douglasii Hook. & Arn.', htmllabel='<i>Quercus douglasii</i> Hook. & Arn.', colparentid='76HN' WHERE colid='4R4TP';
UPDATE tlkptaxon SET newlabel='Quercus ellipsoidalis E.J.Hill', htmllabel='<i>Quercus ellipsoidalis</i> E.J.Hill', colparentid='76HN' WHERE colid='4R4V3';
UPDATE tlkptaxon SET newlabel='Quercus engelmannii Greene', htmllabel='<i>Quercus engelmannii</i> Greene', colparentid='76HN' WHERE colid='4R4VG';
UPDATE tlkptaxon SET newlabel='Quercus falcata Michx.', htmllabel='<i>Quercus falcata</i> Michx.', colparentid='76HN' WHERE colid='4R4WQ';
UPDATE tlkptaxon SET newlabel='Quercus frainetto Ten.', htmllabel='<i>Quercus frainetto</i> Ten.', colparentid='76HN' WHERE colid='4R4YG';
UPDATE tlkptaxon SET newlabel='Quercus grisea Liebm.', htmllabel='<i>Quercus grisea</i> Liebm.', colparentid='76HN' WHERE colid='4R53K';
UPDATE tlkptaxon SET newlabel='Quercus hartwissiana Steven', htmllabel='<i>Quercus hartwissiana</i> Steven', colparentid='76HN' WHERE colid='4R54H';
UPDATE tlkptaxon SET newlabel='Quercus infectoria G.Olivier', htmllabel='<i>Quercus infectoria</i> G.Olivier', colparentid='76HN' WHERE colid='78RPF';
UPDATE tlkptaxon SET newlabel='Quercus infectoria subsp. veneris (A.Kern.) Meikle', htmllabel='<i>Quercus infectoria</i> subsp. <i>veneris</i> (A.Kern.) Meikle', colparentid='78RPF' WHERE colid='5KTR7';
UPDATE tlkptaxon SET newlabel='Quercus ithaburensis Decne.', htmllabel='<i>Quercus ithaburensis</i> Decne.', colparentid='76HN' WHERE colid='4R597';
UPDATE tlkptaxon SET newlabel='Quercus ithaburensis subsp. macrolepis (Kotschy) Hedge & Yalt.', htmllabel='<i>Quercus ithaburensis</i> subsp. <i>macrolepis</i> (Kotschy) Hedge & Yalt.', colparentid='76HN' WHERE colid='5KTRB';
UPDATE tlkptaxon SET newlabel='Quercus lobata Née', htmllabel='<i>Quercus lobata</i> Née', colparentid='76HN' WHERE colid='4R5DZ';
UPDATE tlkptaxon SET newlabel='Quercus lusitanica Lam.', htmllabel='<i>Quercus lusitanica</i> Lam.', colparentid='76HN' WHERE colid='4R5F6';
UPDATE tlkptaxon SET newlabel='Quercus marilandica (L.) Münchh.', htmllabel='<i>Quercus marilandica</i> (L.) Münchh.', colparentid='76HN' WHERE colid='4R5GR';
UPDATE tlkptaxon SET newlabel='Quercus michauxii Nutt.', htmllabel='<i>Quercus michauxii</i> Nutt.', colparentid='76HN' WHERE colid='4R5J6';
UPDATE tlkptaxon SET newlabel='Quercus mongolica subsp. crispula (Blume) Menitsky', htmllabel='<i>Quercus mongolica</i> subsp. <i>crispula</i> (Blume) Menitsky', colparentid='4R5KF' WHERE colid='5KTSB';
UPDATE tlkptaxon SET newlabel='Quercus muehlenbergii Engelm.', htmllabel='<i>Quercus muehlenbergii</i> Engelm.', colparentid='76HN' WHERE colid='4R5L5';
UPDATE tlkptaxon SET newlabel='Quercus nigra L.', htmllabel='<i>Quercus nigra</i> L.', colparentid='76HN' WHERE colid='6X426';
UPDATE tlkptaxon SET newlabel='Quercus petraea (Matt.) Liebl.', htmllabel='<i>Quercus petraea</i> (Matt.) Liebl.', colparentid='76HN' WHERE colid='4R5SK';
UPDATE tlkptaxon SET newlabel='Quercus pontica K.Koch', htmllabel='<i>Quercus pontica</i> K.Koch', colparentid='76HN' WHERE colid='793Q8';
UPDATE tlkptaxon SET newlabel='Quercus robur L.', htmllabel='<i>Quercus robur</i> L.', colparentid='76HN' WHERE colid='4R5YN';
UPDATE tlkptaxon SET newlabel='Quercus rubra L.', htmllabel='<i>Quercus rubra</i> L.', colparentid='76HN' WHERE colid='4R5Z8';
UPDATE tlkptaxon SET newlabel='Quercus shumardii Buckley', htmllabel='<i>Quercus shumardii</i> Buckley', colparentid='76HN' WHERE colid='4R64C';
UPDATE tlkptaxon SET newlabel='Quercus stellata Wangenh.', htmllabel='<i>Quercus stellata</i> Wangenh.', colparentid='76HN' WHERE colid='4R667';
UPDATE tlkptaxon SET newlabel='Quercus suber L.', htmllabel='<i>Quercus suber</i> L.', colparentid='76HN' WHERE colid='4R678';
UPDATE tlkptaxon SET newlabel='Rhamnus L.', htmllabel='<i>Rhamnus</i> L.', colparentid='K9G' WHERE colid='78CD';
UPDATE tlkptaxon SET newlabel='Endotropis crocea subsp. crocea', htmllabel='<i>Endotropis crocea</i> subsp. <i>crocea</i>', colparentid='39SNV' WHERE colid='4RZZQ';
UPDATE tlkptaxon SET newlabel='Rhus L.', htmllabel='<i>Rhus</i> L.', colparentid='J56' WHERE colid='63QFF';
UPDATE tlkptaxon SET newlabel='Rhus ovata S. Watson', htmllabel='<i>Rhus ovata</i> S. Watson', colparentid='63QFF' WHERE colid='4SN87';
UPDATE tlkptaxon SET newlabel='Robinia', htmllabel='<i>Robinia</i>', colparentid='623QT' WHERE colid='63S3X';
UPDATE tlkptaxon SET newlabel='Robinia neomexicana A.Gray', htmllabel='<i>Robinia neomexicana</i> A.Gray', colparentid='63S3X' WHERE colid='4T7YK';
UPDATE tlkptaxon SET newlabel='Robinia pseudoacacia L.', htmllabel='<i>Robinia pseudoacacia</i> L.', colparentid='63S3X' WHERE colid='4T7YV';
UPDATE tlkptaxon SET newlabel='Salix L.', htmllabel='<i>Salix</i> L.', colparentid='628MQ' WHERE colid='7BWB';
UPDATE tlkptaxon SET newlabel='Salix amygdaloides Andersson', htmllabel='<i>Salix amygdaloides</i> Andersson', colparentid='7BWB' WHERE colid='6XCK4';
UPDATE tlkptaxon SET newlabel='Salix candida Flüggé ex Willd.', htmllabel='<i>Salix candida</i> Flüggé ex Willd.', colparentid='7BWB' WHERE colid='79CBB';
UPDATE tlkptaxon SET newlabel='Salix caroliniana Michx.', htmllabel='<i>Salix caroliniana</i> Michx.', colparentid='7BWB' WHERE colid='6XCBP';
UPDATE tlkptaxon SET newlabel='Salix discolor Muhl.', htmllabel='<i>Salix discolor</i> Muhl.', colparentid='7BWB' WHERE colid='6XCD9';
UPDATE tlkptaxon SET newlabel='Salix elaeagnos Scop.', htmllabel='<i>Salix elaeagnos</i> Scop.', colparentid='7BWB' WHERE colid='79C5X';
UPDATE tlkptaxon SET newlabel='Salix exigua Nutt.', htmllabel='<i>Salix exigua</i> Nutt.', colparentid='7BWB' WHERE colid='6XC73';
UPDATE tlkptaxon SET newlabel='Salix glauca L.', htmllabel='<i>Salix glauca</i> L.', colparentid='7BWB' WHERE colid='79C5K';
UPDATE tlkptaxon SET newlabel='Salix interior Rowlee', htmllabel='<i>Salix interior</i> Rowlee', colparentid='7BWB' WHERE colid='6XC94';
UPDATE tlkptaxon SET newlabel='Salix lanata L.', htmllabel='<i>Salix lanata</i> L.', colparentid='7BWB' WHERE colid='79BVQ';
UPDATE tlkptaxon SET newlabel='Salix lasiolepis Benth.', htmllabel='<i>Salix lasiolepis</i> Benth.', colparentid='7BWB' WHERE colid='6XC7R';
UPDATE tlkptaxon SET newlabel='Salix myrsinifolia Salisb.', htmllabel='<i>Salix myrsinifolia</i> Salisb.', colparentid='7BWB' WHERE colid='6XC9W';
UPDATE tlkptaxon SET newlabel='Salix ×  pendulina Wender.', htmllabel='<i>Salix ×  pendulina</i> Wender.', colparentid='7BWB' WHERE colid='6XDC4';
UPDATE tlkptaxon SET newlabel='Salix purpurea L.', htmllabel='<i>Salix purpurea</i> L.', colparentid='7BWB' WHERE colid='79DST';
UPDATE tlkptaxon SET newlabel='Salix viminalis L.', htmllabel='<i>Salix viminalis</i> L.', colparentid='7BWB' WHERE colid='6XDMG';
UPDATE tlkptaxon SET newlabel='Santalaceae R. Br.', htmllabel='Santalaceae R. Br.', colparentid='3ZX' WHERE colid='FXV';
UPDATE tlkptaxon SET newlabel='Santalales R. Br. ex Bercht. & J. Presl', htmllabel='Santalales R. Br. ex Bercht. & J. Presl', colparentid='MG' WHERE colid='3ZX';
UPDATE tlkptaxon SET newlabel='Santalum L.', htmllabel='<i>Santalum</i> L.', colparentid='FXV' WHERE colid='7C8S';
UPDATE tlkptaxon SET newlabel='Santalum album L.', htmllabel='<i>Santalum album</i> L.', colparentid='7C8S' WHERE colid='6XJ9M';
UPDATE tlkptaxon SET newlabel='Saxegothaea', htmllabel='<i>Saxegothaea</i>', colparentid='627HD' WHERE colid='7CV2';
UPDATE tlkptaxon SET newlabel='Sciadopityaceae', htmllabel='Sciadopityaceae', colparentid='623FD' WHERE colid='G3X';
UPDATE tlkptaxon SET newlabel='Sciadopitys', htmllabel='<i>Sciadopitys</i>', colparentid='G3X' WHERE colid='7DZN';
UPDATE tlkptaxon SET newlabel='Sciadopitys verticillata (Thunb.) Siebold & Zucc.', htmllabel='<i>Sciadopitys verticillata</i> (Thunb.) Siebold & Zucc.', colparentid='7DZN' WHERE colid='4VGZH';
UPDATE tlkptaxon SET newlabel='Sequoia', htmllabel='<i>Sequoia</i>', colparentid='8SY' WHERE colid='63SMF';
UPDATE tlkptaxon SET newlabel='Sequoia sempervirens (D. Don) Endl.', htmllabel='<i>Sequoia sempervirens</i> (D. Don) Endl.', colparentid='63SMF' WHERE colid='4WSQG';
UPDATE tlkptaxon SET newlabel='Sequoiadendron', htmllabel='<i>Sequoiadendron</i>', colparentid='8SY' WHERE colid='7FQF';
UPDATE tlkptaxon SET newlabel='Shorea Roxb.', htmllabel='<i>Shorea</i> Roxb.', colparentid='628JF' WHERE colid='7GFX';
UPDATE tlkptaxon SET newlabel='Shorea robusta Gaertn.', htmllabel='<i>Shorea robusta</i> Gaertn.', colparentid='7GFX' WHERE colid='4X52Q';
UPDATE tlkptaxon SET newlabel='Swartzia', htmllabel='<i>Swartzia</i>', colparentid='623QT' WHERE colid='7QKG';
UPDATE tlkptaxon SET newlabel='Swartzia laevicarpa Amshoff', htmllabel='<i>Swartzia laevicarpa</i> Amshoff', colparentid='7QKG' WHERE colid='53JDN';
UPDATE tlkptaxon SET newlabel='Swietenia Jacq.', htmllabel='<i>Swietenia</i> Jacq.', colparentid='626TL' WHERE colid='7QL2';
UPDATE tlkptaxon SET newlabel='Swietenia macrophylla G. King', htmllabel='<i>Swietenia macrophylla</i> G. King', colparentid='7QL2' WHERE colid='53K5Y';
UPDATE tlkptaxon SET newlabel='Swietenia mahagoni (L.) Jacq.', htmllabel='<i>Swietenia mahagoni</i> (L.) Jacq.', colparentid='7QL2' WHERE colid='53K5Z';
UPDATE tlkptaxon SET newlabel='Tamaricaceae Link', htmllabel='Tamaricaceae Link', colparentid='VW' WHERE colid='GWM';
UPDATE tlkptaxon SET newlabel='Tamarix L.', htmllabel='<i>Tamarix</i> L.', colparentid='GWM' WHERE colid='7S9V';
UPDATE tlkptaxon SET newlabel='Tamarix chinensis Lour.', htmllabel='<i>Tamarix chinensis</i> Lour.', colparentid='7S9V' WHERE colid='7BJ8N';
UPDATE tlkptaxon SET newlabel='Taxaceae', htmllabel='Taxaceae', colparentid='623FD' WHERE colid='GXW';
UPDATE tlkptaxon SET newlabel='Taxodium', htmllabel='<i>Taxodium</i>', colparentid='8SY' WHERE colid='7T6Q';
UPDATE tlkptaxon SET newlabel='Taxodium distichum (L.) Rich.', htmllabel='<i>Taxodium distichum</i> (L.) Rich.', colparentid='7T6Q' WHERE colid='5526G';
UPDATE tlkptaxon SET newlabel='Taxodium mucronatum Ten.', htmllabel='<i>Taxodium mucronatum</i> Ten.', colparentid='7T6Q' WHERE colid='5526P';
UPDATE tlkptaxon SET newlabel='Taxus', htmllabel='<i>Taxus</i>', colparentid='GXW' WHERE colid='63TKC';
UPDATE tlkptaxon SET newlabel='Taxus cuspidata Siebold & Zucc.', htmllabel='<i>Taxus cuspidata</i> Siebold & Zucc.', colparentid='63TKC' WHERE colid='55284';
UPDATE tlkptaxon SET newlabel='Tectona grandis L.f.', htmllabel='<i>Tectona grandis</i> L.f.', colparentid='7T9P' WHERE colid='553LY';
UPDATE tlkptaxon SET newlabel='Tetraclinis', htmllabel='<i>Tetraclinis</i>', colparentid='8SY' WHERE colid='64736';
UPDATE tlkptaxon SET newlabel='Tetraclinis articulata (Vahl) Mast.', htmllabel='<i>Tetraclinis articulata</i> (Vahl) Mast.', colparentid='64736' WHERE colid='55LNG';
UPDATE tlkptaxon SET newlabel='Thuja', htmllabel='<i>Thuja</i>', colparentid='8SY' WHERE colid='648D5';
UPDATE tlkptaxon SET newlabel='Thuja occidentalis L.', htmllabel='<i>Thuja occidentalis</i> L.', colparentid='648D5' WHERE colid='56NTP';
UPDATE tlkptaxon SET newlabel='Quercus macrocarpa Michx.', htmllabel='<i>Quercus macrocarpa</i> Michx.', colparentid='76HN' WHERE colid='4R5FS';
UPDATE tlkptaxon SET newlabel='Tilia cordata Mill.', htmllabel='<i>Tilia cordata</i> Mill.', colparentid='7WX3' WHERE colid='56WPP';
UPDATE tlkptaxon SET newlabel='Abies chensiensis Tiegh.', htmllabel='<i>Abies chensiensis</i> Tiegh.', colparentid='627WF' WHERE colid='8KBB';
UPDATE tlkptaxon SET newlabel='Abies grandis (Douglas ex D. Don) Lindl.', htmllabel='<i>Abies grandis</i> (Douglas ex D. Don) Lindl.', colparentid='627WF' WHERE colid='8KCY';
UPDATE tlkptaxon SET newlabel='Abies lasiocarpa (Hook.) Nutt.', htmllabel='<i>Abies lasiocarpa</i> (Hook.) Nutt.', colparentid='627WF' WHERE colid='8KDX';
UPDATE tlkptaxon SET newlabel='Abies mariesii Mast.', htmllabel='<i>Abies mariesii</i> Mast.', colparentid='627WF' WHERE colid='63YVK';
UPDATE tlkptaxon SET newlabel='Abies veitchii Lindl.', htmllabel='<i>Abies veitchii</i> Lindl.', colparentid='627WF' WHERE colid='63Z85';
UPDATE tlkptaxon SET newlabel='Allocasuarina littoralis (Salisb.) L. A. S. Johnson', htmllabel='<i>Allocasuarina littoralis</i> (Salisb.) L. A. S. Johnson', colparentid='S78' WHERE colid='BVC9';
UPDATE tlkptaxon SET newlabel='Alnus alnobetula subsp. alnobetula', htmllabel='<i>Alnus alnobetula</i> subsp. <i>alnobetula</i>', colparentid='C2NF' WHERE colid='C2TP';
UPDATE tlkptaxon SET newlabel='n/a', htmllabel='n/a', colparentid='n/a' WHERE colid='';
UPDATE tlkptaxon SET newlabel='Apiales', htmllabel='Apiales', colparentid='MG' WHERE colid='Q3';
UPDATE tlkptaxon SET newlabel='Araliaceae', htmllabel='Araliaceae', colparentid='Q3' WHERE colid='6MC';
UPDATE tlkptaxon SET newlabel='Araucaria bidwillii Hook.', htmllabel='<i>Araucaria bidwillii</i> Hook.', colparentid='ZSD' WHERE colid='G67F';
UPDATE tlkptaxon SET newlabel='Arbutus L.', htmllabel='<i>Arbutus</i> L.', colparentid='J65' WHERE colid='62CRL';
UPDATE tlkptaxon SET newlabel='Arbutus arizonica (A. Gray) Sarg.', htmllabel='<i>Arbutus arizonica</i> (A. Gray) Sarg.', colparentid='62CRL' WHERE colid='G6P5';
UPDATE tlkptaxon SET newlabel='Balanites Delile', htmllabel='<i>Balanites</i> Delile', colparentid='J7G' WHERE colid='37HJ';
UPDATE tlkptaxon SET newlabel='Balanites aegyptiaca (L.) Delile', htmllabel='<i>Balanites aegyptiaca</i> (L.) Delile', colparentid='37HJ' WHERE colid='KFPN';
UPDATE tlkptaxon SET newlabel='Berberidaceae Juss.', htmllabel='Berberidaceae Juss.', colparentid='3XS' WHERE colid='76G';
UPDATE tlkptaxon SET newlabel='Berberis L.', htmllabel='<i>Berberis</i> L.', colparentid='J7X' WHERE colid='39LV';
UPDATE tlkptaxon SET newlabel='Berberis vulgaris L.', htmllabel='<i>Berberis vulgaris</i> L.', colparentid='39LV' WHERE colid='LKBJ';
UPDATE tlkptaxon SET newlabel='Boraginaceae Juss.', htmllabel='Boraginaceae Juss.', colparentid='TW' WHERE colid='622G7';
UPDATE tlkptaxon SET newlabel='Carya cordiformis (Wangenh.) C. Koch', htmllabel='<i>Carya cordiformis</i> (Wangenh.) C. Koch', colparentid='3HXZ' WHERE colid='RHS3';
UPDATE tlkptaxon SET newlabel='Carya illinoiensis (Wangenh.) K. Koch', htmllabel='<i>Carya illinoiensis</i> (Wangenh.) K. Koch', colparentid='3HXZ' WHERE colid='5XDHD';
UPDATE tlkptaxon SET newlabel='Cordia L.', htmllabel='<i>Cordia</i> L.', colparentid='9L9' WHERE colid='62MK7';
UPDATE tlkptaxon SET newlabel='Cordia alliodora (Ruiz & Pav.) Oken', htmllabel='<i>Cordia alliodora</i> (Ruiz & Pav.) Oken', colparentid='62MK7' WHERE colid='YB6S';
UPDATE tlkptaxon SET newlabel='Elaeoluma', htmllabel='<i>Elaeoluma</i>', colparentid='FY4' WHERE colid='6332D';
UPDATE tlkptaxon SET newlabel='Ginkgo', htmllabel='<i>Ginkgo</i>', colparentid='623HJ' WHERE colid='4NL8';
UPDATE tlkptaxon SET newlabel='Ginkgo biloba L.', htmllabel='<i>Ginkgo biloba</i> L.', colparentid='4NL8' WHERE colid='3G3B3';
UPDATE tlkptaxon SET newlabel='Ginkgoaceae', htmllabel='Ginkgoaceae', colparentid='39Q' WHERE colid='623HJ';
UPDATE tlkptaxon SET newlabel='Ginkgoales', htmllabel='Ginkgoales', colparentid='BT' WHERE colid='39Q';
UPDATE tlkptaxon SET newlabel='Ginkgoopsida', htmllabel='Ginkgoopsida', colparentid='TP' WHERE colid='BT';
UPDATE tlkptaxon SET newlabel='Guibourtia', htmllabel='<i>Guibourtia</i>', colparentid='623QT' WHERE colid='4RG7';
UPDATE tlkptaxon SET newlabel='Guibourtia coleosperma (Benth.) Leonard', htmllabel='<i>Guibourtia coleosperma</i> (Benth.) Leonard', colparentid='4RG7' WHERE colid='3HKMT';
UPDATE tlkptaxon SET newlabel='Hamamelis L.', htmllabel='<i>Hamamelis</i> L.', colparentid='JMY' WHERE colid='635R7';
UPDATE tlkptaxon SET newlabel='Hamamelis virginiana L.', htmllabel='<i>Hamamelis virginiana</i> L.', colparentid='635R7' WHERE colid='3JH4V';
UPDATE tlkptaxon SET newlabel='Juniperus occidentalis Hook.', htmllabel='<i>Juniperus occidentalis</i> Hook.', colparentid='56YL' WHERE colid='6NFYT';
UPDATE tlkptaxon SET newlabel='Juniperus phoenicea L.', htmllabel='<i>Juniperus phoenicea</i> L.', colparentid='56YL' WHERE colid='6NFYQ';
UPDATE tlkptaxon SET newlabel='Juniperus pingii W. C. Cheng', htmllabel='<i>Juniperus pingii</i> W. C. Cheng', colparentid='56YL' WHERE colid='6NFYK';
UPDATE tlkptaxon SET newlabel='Juniperus pingii var. pingii', htmllabel='<i>Juniperus pingii</i> var. <i>pingii</i>', colparentid='6NFYK' WHERE colid='5PKYG';
UPDATE tlkptaxon SET newlabel='Larix potaninii Batalin', htmllabel='<i>Larix potaninii</i> Batalin', colparentid='639D7' WHERE colid='6NYY7';
UPDATE tlkptaxon SET newlabel='Leucaena', htmllabel='<i>Leucaena</i>', colparentid='623QT' WHERE colid='5DQJ';
UPDATE tlkptaxon SET newlabel='Leucaena leucocephala (Lam.) de Wit', htmllabel='<i>Leucaena leucocephala</i> (Lam.) de Wit', colparentid='5DQJ' WHERE colid='724CH';
UPDATE tlkptaxon SET newlabel='Magnolia acuminata (L.) L.', htmllabel='<i>Magnolia acuminata</i> (L.) L.', colparentid='5K5W' WHERE colid='3XH5C';
UPDATE tlkptaxon SET newlabel='Ostrya virginiana (Mill.) K.Koch', htmllabel='<i>Ostrya virginiana</i> (Mill.) K.Koch', colparentid='63H3Q' WHERE colid='75D9H';
UPDATE tlkptaxon SET newlabel='Picea schrenkiana subsp. tianschanica (Rupr.) Bykov', htmllabel='<i>Picea schrenkiana</i> subsp. <i>tianschanica</i> (Rupr.) Bykov', colparentid='6VJ2C' WHERE colid='7KMH9';
UPDATE tlkptaxon SET newlabel='Pinus arizonica Engelm.', htmllabel='<i>Pinus arizonica</i> Engelm.', colparentid='6QPY' WHERE colid='4J22P';
UPDATE tlkptaxon SET newlabel='Pinus arizonica var. cooperi (C. E. Blanco) Farjon', htmllabel='<i>Pinus arizonica</i> var. <i>cooperi</i> (C. E. Blanco) Farjon', colparentid='4J22P' WHERE colid='5QR6F';
UPDATE tlkptaxon SET newlabel='Pinus caribaea Morelet', htmllabel='<i>Pinus caribaea</i> Morelet', colparentid='6QPY' WHERE colid='4J246';
UPDATE tlkptaxon SET newlabel='Pinus cembroides Zucc.', htmllabel='<i>Pinus cembroides</i> Zucc.', colparentid='6QPY' WHERE colid='4J24K';
UPDATE tlkptaxon SET newlabel='Pinus rigida Mill.', htmllabel='<i>Pinus rigida</i> Mill.', colparentid='6QPY' WHERE colid='4J2G9';
UPDATE tlkptaxon SET newlabel='Pinus sibirica Du Tour', htmllabel='<i>Pinus sibirica</i> Du Tour', colparentid='6QPY' WHERE colid='4J2HD';
UPDATE tlkptaxon SET newlabel='Pinus strobus L.', htmllabel='<i>Pinus strobus</i> L.', colparentid='6QPY' WHERE colid='4J2HY';
UPDATE tlkptaxon SET newlabel='Platycladus orientalis (L.) Franco', htmllabel='<i>Platycladus orientalis</i> (L.) Franco', colparentid='6RQZ' WHERE colid='4JPPV';
UPDATE tlkptaxon SET newlabel='Podocarpus nubigenus Lindl.', htmllabel='<i>Podocarpus nubigenus</i> Lindl.', colparentid='6TC9' WHERE colid='4KQ5H';
UPDATE tlkptaxon SET newlabel='Polyscias', htmllabel='<i>Polyscias</i>', colparentid='6MC' WHERE colid='63P67';
UPDATE tlkptaxon SET newlabel='Polyscias sambucifolia (Sieber ex DC.) Harms', htmllabel='<i>Polyscias sambucifolia</i> (Sieber ex DC.) Harms', colparentid='63P67' WHERE colid='4LLVQ';
UPDATE tlkptaxon SET newlabel='Prumnopitys ferruginea (G. Benn. ex D. Don) de Laub.', htmllabel='<i>Prumnopitys ferruginea</i> (G. Benn. ex D. Don) de Laub.', colparentid='6Y68' WHERE colid='4N89Z';
UPDATE tlkptaxon SET newlabel='Prunus serotina Ehrh.', htmllabel='<i>Prunus serotina</i> Ehrh.', colparentid='6Y6H' WHERE colid='4N97Q';
UPDATE tlkptaxon SET newlabel='Quercus arizonica Sarg.', htmllabel='<i>Quercus arizonica</i> Sarg.', colparentid='76HN' WHERE colid='6X3QQ';
UPDATE tlkptaxon SET newlabel='Quercus cerris L.', htmllabel='<i>Quercus cerris</i> L.', colparentid='76HN' WHERE colid='4R4KH';
UPDATE tlkptaxon SET newlabel='Quercus chrysolepis Liebm.', htmllabel='<i>Quercus chrysolepis</i> Liebm.', colparentid='76HN' WHERE colid='4R4LC';
UPDATE tlkptaxon SET newlabel='Quercus copeyensis C.H.Mull.', htmllabel='<i>Quercus copeyensis</i> C.H.Mull.', colparentid='76HN' WHERE colid='4R4NR';
UPDATE tlkptaxon SET newlabel='Quercus gambelii Nutt.', htmllabel='<i>Quercus gambelii</i> Nutt.', colparentid='76HN' WHERE colid='4R4Z8';
UPDATE tlkptaxon SET newlabel='Quercus montana Willd.', htmllabel='<i>Quercus montana</i> Willd.', colparentid='76HN' WHERE colid='4R5KM';
UPDATE tlkptaxon SET newlabel='Ranunculales Juss. ex Bercht. & J. Presl', htmllabel='Ranunculales Juss. ex Bercht. & J. Presl', colparentid='MG' WHERE colid='3XS';
UPDATE tlkptaxon SET newlabel='Sassafras J. Presl', htmllabel='<i>Sassafras</i> J. Presl', colparentid='BTB' WHERE colid='643PF';
UPDATE tlkptaxon SET newlabel='Sassafras albidum (Nutt.) Nees', htmllabel='<i>Sassafras albidum</i> (Nutt.) Nees', colparentid='643PF' WHERE colid='79QLB';
UPDATE tlkptaxon SET newlabel='Tamarix aphylla (L.) Karst.', htmllabel='<i>Tamarix aphylla</i> (L.) Karst.', colparentid='7S9V' WHERE colid='54M9Z';
UPDATE tlkptaxon SET newlabel='Thuja plicata Donn ex D. Don', htmllabel='<i>Thuja plicata</i> Donn ex D. Don', colparentid='648D5' WHERE colid='56NTV';
UPDATE tlkptaxon SET newlabel='Thuja standishii (Gordon) Carrière', htmllabel='<i>Thuja standishii</i> (Gordon) Carrière', colparentid='648D5' WHERE colid='56NTZ';
UPDATE tlkptaxon SET newlabel='Thujopsis', htmllabel='<i>Thujopsis</i>', colparentid='8SY' WHERE colid='63WD4';
UPDATE tlkptaxon SET newlabel='Tilia L.', htmllabel='<i>Tilia</i> L.', colparentid='KFH' WHERE colid='7WX3';
UPDATE tlkptaxon SET newlabel='Tilia americana L.', htmllabel='<i>Tilia americana</i> L.', colparentid='7WX3' WHERE colid='56WNF';
UPDATE tlkptaxon SET newlabel='Tilia platyphyllos Scop.', htmllabel='<i>Tilia platyphyllos</i> Scop.', colparentid='7WX3' WHERE colid='7CC2T';
UPDATE tlkptaxon SET newlabel='Torreya', htmllabel='<i>Torreya</i>', colparentid='GXW' WHERE colid='7XWB';
UPDATE tlkptaxon SET newlabel='Torreya californica Torr.', htmllabel='<i>Torreya californica</i> Torr.', colparentid='7XWB' WHERE colid='57GND';
UPDATE tlkptaxon SET newlabel='Tsuga', htmllabel='<i>Tsuga</i>', colparentid='625M7' WHERE colid='6486F';
UPDATE tlkptaxon SET newlabel='Tsuga canadensis (L.) Carrière', htmllabel='<i>Tsuga canadensis</i> (L.) Carrière', colparentid='6486F' WHERE colid='59HM6';
UPDATE tlkptaxon SET newlabel='Tsuga caroliniana Engelm.', htmllabel='<i>Tsuga caroliniana</i> Engelm.', colparentid='6486F' WHERE colid='59HM7';
UPDATE tlkptaxon SET newlabel='Tsuga chinensis (Franch.) E. Pritz.', htmllabel='<i>Tsuga chinensis</i> (Franch.) E. Pritz.', colparentid='6486F' WHERE colid='59HM8';
UPDATE tlkptaxon SET newlabel='Tsuga diversifolia (Maxim.) Mast.', htmllabel='<i>Tsuga diversifolia</i> (Maxim.) Mast.', colparentid='6486F' WHERE colid='7D9VC';
UPDATE tlkptaxon SET newlabel='Tsuga dumosa (D. Don) Eichler', htmllabel='<i>Tsuga dumosa</i> (D. Don) Eichler', colparentid='6486F' WHERE colid='59HMC';
UPDATE tlkptaxon SET newlabel='Tsuga heterophylla (Raf.) Sarg.', htmllabel='<i>Tsuga heterophylla</i> (Raf.) Sarg.', colparentid='6486F' WHERE colid='59HMG';
UPDATE tlkptaxon SET newlabel='Tsuga mertensiana (Bong.) Carrière', htmllabel='<i>Tsuga mertensiana</i> (Bong.) Carrière', colparentid='6486F' WHERE colid='59HMR';
UPDATE tlkptaxon SET newlabel='Typhloscolecidae', htmllabel='Typhloscolecidae', colparentid='3TG' WHERE colid='HMT';
UPDATE tlkptaxon SET newlabel='Ulmus L.', htmllabel='<i>Ulmus</i> L.', colparentid='HNB' WHERE colid='84MS';
UPDATE tlkptaxon SET newlabel='Ulmus glabra Huds.', htmllabel='<i>Ulmus glabra</i> Huds.', colparentid='84MS' WHERE colid='7DFJZ';
UPDATE tlkptaxon SET newlabel='Ulmus procera Salisb.', htmllabel='<i>Ulmus procera</i> Salisb.', colparentid='84MS' WHERE colid='7DFMR';
UPDATE tlkptaxon SET newlabel='Ulmus pumila L.', htmllabel='<i>Ulmus pumila</i> L.', colparentid='84MS' WHERE colid='7DFP7';
UPDATE tlkptaxon SET newlabel='Ulmus rubra Muhl.', htmllabel='<i>Ulmus rubra</i> Muhl.', colparentid='84MS' WHERE colid='7DFPH';
UPDATE tlkptaxon SET newlabel='Viburnum L.', htmllabel='<i>Viburnum</i> L.', colparentid='628MM' WHERE colid='86MY';
UPDATE tlkptaxon SET newlabel='Viburnum lantana L.', htmllabel='<i>Viburnum lantana</i> L.', colparentid='86MY' WHERE colid='5BC9Y';
UPDATE tlkptaxon SET newlabel='Vouacapoua', htmllabel='<i>Vouacapoua</i>', colparentid='623QT' WHERE colid='647Q2';
UPDATE tlkptaxon SET newlabel='Widdringtonia', htmllabel='<i>Widdringtonia</i>', colparentid='8SY' WHERE colid='87X6';
UPDATE tlkptaxon SET newlabel='Widdringtonia cedarbergensis J. A. Marsh', htmllabel='<i>Widdringtonia cedarbergensis</i> J. A. Marsh', colparentid='87X6' WHERE colid='5BYG9';
UPDATE tlkptaxon SET newlabel='Zygophyllaceae R. Br.', htmllabel='Zygophyllaceae R. Br.', colparentid='4CB' WHERE colid='J39';
UPDATE tlkptaxon SET newlabel='Zygophyllum L.', htmllabel='<i>Zygophyllum</i> L.', colparentid='KJG' WHERE colid='8BDZ';
UPDATE tlkptaxon SET newlabel='Isoberlinia doka Craib & Stapf', htmllabel='<i>Isoberlinia doka</i> Craib & Stapf', colparentid='55DT' WHERE colid='3Q7H8';
UPDATE tlkptaxon SET newlabel='Juniperus deppeana Steud.', htmllabel='<i>Juniperus deppeana</i> Steud.', colparentid='56YL' WHERE colid='6NGGD';
UPDATE tlkptaxon SET newlabel='Terminalia leiocarpa (DC.) Baill.', htmllabel='<i>Terminalia leiocarpa</i> (DC.) Baill.', colparentid='647GM' WHERE colid='55G93';
UPDATE tlkptaxon SET newlabel='Adenostoma fasciculatum Hook. & Arn.', htmllabel='<i>Adenostoma fasciculatum</i> Hook. & Arn.', colparentid='P7R' WHERE colid='652F2';
UPDATE tlkptaxon SET newlabel='Albizia lebbeck (L.) Benth.', htmllabel='<i>Albizia lebbeck</i> (L.) Benth.', colparentid='RJB' WHERE colid='BG7V';
UPDATE tlkptaxon SET newlabel='Alnus', htmllabel='<i>Alnus</i>', colparentid='777' WHERE colid='SQX';
UPDATE tlkptaxon SET newlabel='Alnus alnobetula subsp. sinuata (Regel) Raus', htmllabel='<i>Alnus alnobetula</i> subsp. <i>sinuata</i> (Regel) Raus', colparentid='C2NF' WHERE colid='5FHWK';
UPDATE tlkptaxon SET newlabel='Alnus hirsuta (Spach) Rupr.', htmllabel='<i>Alnus hirsuta</i> (Spach) Rupr.', colparentid='SQX' WHERE colid='C2Q9';
UPDATE tlkptaxon SET newlabel='Athrotaxis', htmllabel='<i>Athrotaxis</i>', colparentid='8SY' WHERE colid='352R';
UPDATE tlkptaxon SET newlabel='Bauhinia reticulata DC.', htmllabel='<i>Bauhinia reticulata</i> DC.', colparentid='38XT' WHERE colid='5WFYW';
UPDATE tlkptaxon SET newlabel='Bauhinia rufescens Lam.', htmllabel='<i>Bauhinia rufescens</i> Lam.', colparentid='38XT' WHERE colid='5WFJX';
UPDATE tlkptaxon SET newlabel='Betula glandulosa Michx.', htmllabel='<i>Betula glandulosa</i> Michx.', colparentid='62GLV' WHERE colid='LP8B';
UPDATE tlkptaxon SET newlabel='Burkea africana Hook.', htmllabel='<i>Burkea africana</i> Hook.', colparentid='62HQ5' WHERE colid='NXGS';
UPDATE tlkptaxon SET newlabel='Cedrus libani var. brevifolia Hook. f.', htmllabel='<i>Cedrus libani</i> var. <i>brevifolia</i> Hook. f.', colparentid='S24H' WHERE colid='5MXZ7';
UPDATE tlkptaxon SET newlabel='Combretum Loefl.', htmllabel='<i>Combretum</i> Loefl.', colparentid='JD4' WHERE colid='3SRG';
UPDATE tlkptaxon SET newlabel='Combretum glutinosum Perr. ex DC.', htmllabel='<i>Combretum glutinosum</i> Perr. ex DC.', colparentid='3SRG' WHERE colid='5ZRBL';
UPDATE tlkptaxon SET newlabel='Cotinus coggygria Scop.', htmllabel='<i>Cotinus coggygria</i> Scop.', colparentid='62NHK' WHERE colid='YXRZ';
UPDATE tlkptaxon SET newlabel='Daniellia oliveri (Rolfe) Hutch. & Dalziel', htmllabel='<i>Daniellia oliveri</i> (Rolfe) Hutch. & Dalziel', colparentid='3ZR7' WHERE colid='343WR';
UPDATE tlkptaxon SET newlabel='Delonix', htmllabel='<i>Delonix</i>', colparentid='623QT' WHERE colid='42PH';
UPDATE tlkptaxon SET newlabel='Delonix regia (Hook.) Raf.', htmllabel='<i>Delonix regia</i> (Hook.) Raf.', colparentid='42PH' WHERE colid='34H4K';
UPDATE tlkptaxon SET newlabel='Detarium', htmllabel='<i>Detarium</i>', colparentid='623QT' WHERE colid='43TF';
UPDATE tlkptaxon SET newlabel='Detarium microcarpum Guill. & Perr.', htmllabel='<i>Detarium microcarpum</i> Guill. & Perr.', colparentid='43TF' WHERE colid='6CNPB';
UPDATE tlkptaxon SET newlabel='Elaeoluma glabrescens (Mart. & Eichler) Aubrév.', htmllabel='<i>Elaeoluma glabrescens</i> (Mart. & Eichler) Aubrév.', colparentid='6332D' WHERE colid='38YDK';
UPDATE tlkptaxon SET newlabel='Faidherbia albida (Delile) A.Chev.', htmllabel='<i>Faidherbia albida</i> (Delile) A.Chev.', colparentid='4J8B' WHERE colid='3DSL4';
UPDATE tlkptaxon SET newlabel='Ficus L.', htmllabel='<i>Ficus</i> L.', colparentid='CXM' WHERE colid='4JYX';
UPDATE tlkptaxon SET newlabel='Ficus sycomorus L.', htmllabel='<i>Ficus sycomorus</i> L.', colparentid='4JYX' WHERE colid='6HXV3';
UPDATE tlkptaxon SET newlabel='Frangula caroliniana (Walter) A. Gray', htmllabel='<i>Frangula caroliniana</i> (Walter) A. Gray', colparentid='4KZF' WHERE colid='6JLHK';
UPDATE tlkptaxon SET newlabel='Lannea acida A. Rich.', htmllabel='<i>Lannea acida</i> A. Rich.', colparentid='62XM8' WHERE colid='6NWM4';
UPDATE tlkptaxon SET newlabel='Larix occidentalis Nutt.', htmllabel='<i>Larix occidentalis</i> Nutt.', colparentid='639D7' WHERE colid='6NYY5';
UPDATE tlkptaxon SET newlabel='Malvales Juss. ex Bercht. & J. Presl', htmllabel='Malvales Juss. ex Bercht. & J. Presl', colparentid='MG' WHERE colid='3HP';
UPDATE tlkptaxon SET newlabel='Nothofagaceae', htmllabel='Nothofagaceae', colparentid='384' WHERE colid='626G3';
UPDATE tlkptaxon SET newlabel='Nothofagus dombeyi (Mirb.) Oerst.', htmllabel='<i>Nothofagus dombeyi</i> (Mirb.) Oerst.', colparentid='654M' WHERE colid='47SZJ';
UPDATE tlkptaxon SET newlabel='Pericopsis laxiflora (Baker) Meeuwen', htmllabel='<i>Pericopsis laxiflora</i> (Baker) Meeuwen', colparentid='6L84' WHERE colid='76SMX';
UPDATE tlkptaxon SET newlabel='Picea jezoensis (Siebold & Zucc.) Carrière', htmllabel='<i>Picea jezoensis</i> (Siebold & Zucc.) Carrière', colparentid='6Q7C' WHERE colid='4HQ39';
UPDATE tlkptaxon SET newlabel='Pinus cembroides subsp. lagunae (Rob.-Pass.) D.K. Bailey', htmllabel='<i>Pinus cembroides</i> subsp. <i>lagunae</i> (Rob.-Pass.) D.K. Bailey', colparentid='4J24K' WHERE colid='5KG8Z';
UPDATE tlkptaxon SET newlabel='Pinus engelmannii Carrière', htmllabel='<i>Pinus engelmannii</i> Carrière', colparentid='6QPY' WHERE colid='4J26G';
UPDATE tlkptaxon SET newlabel='Pinus herrerae Martínez', htmllabel='<i>Pinus herrerae</i> Martínez', colparentid='6QPY' WHERE colid='77L5M';
UPDATE tlkptaxon SET newlabel='Pinus leiophylla var. chihuahuana (Engelm.) Shaw', htmllabel='<i>Pinus leiophylla</i> var. <i>chihuahuana</i> (Engelm.) Shaw', colparentid='6VL5T' WHERE colid='5QRBQ';
UPDATE tlkptaxon SET newlabel='Pinus pinaster subsp. escarena (Risso) K. Richt.', htmllabel='<i>Pinus pinaster</i> subsp. <i>escarena</i> (Risso) K. Richt.', colparentid='77KSH' WHERE colid='5KGB8';
UPDATE tlkptaxon SET newlabel='Pinus wallichiana A.B. Jacks.', htmllabel='<i>Pinus wallichiana</i> A.B. Jacks.', colparentid='6QPY' WHERE colid='4J2KS';
UPDATE tlkptaxon SET newlabel='Prumnopitys andina (Poepp. ex Endl.) de Laub.', htmllabel='<i>Prumnopitys andina</i> (Poepp. ex Endl.) de Laub.', colparentid='6Y68' WHERE colid='4N89W';
UPDATE tlkptaxon SET newlabel='Quercus bicolor Willd.', htmllabel='<i>Quercus bicolor</i> Willd.', colparentid='76HN' WHERE colid='793QP';
UPDATE tlkptaxon SET newlabel='Senegalia', htmllabel='<i>Senegalia</i>', colparentid='623QT' WHERE colid='63SM7';
UPDATE tlkptaxon SET newlabel='Sterculia L.', htmllabel='<i>Sterculia</i> L.', colparentid='KDB' WHERE colid='7N5T';
UPDATE tlkptaxon SET newlabel='Sterculia setigera Delile', htmllabel='<i>Sterculia setigera</i> Delile', colparentid='7N5T' WHERE colid='52DLK';
UPDATE tlkptaxon SET newlabel='Tamarindus', htmllabel='<i>Tamarindus</i>', colparentid='623QT' WHERE colid='7S9Q';
UPDATE tlkptaxon SET newlabel='Tectona', htmllabel='<i>Tectona</i>', colparentid='BR9' WHERE colid='7T9P';
UPDATE tlkptaxon SET newlabel='Terminalia L.', htmllabel='<i>Terminalia</i> L.', colparentid='JD4' WHERE colid='647GM';
UPDATE tlkptaxon SET newlabel='Ziziphus Mill.', htmllabel='<i>Ziziphus</i> Mill.', colparentid='KJF' WHERE colid='648P5';
UPDATE tlkptaxon SET newlabel='Ziziphus mauritiana Lam.', htmllabel='<i>Ziziphus mauritiana</i> Lam.', colparentid='648P5' WHERE colid='5D5ZY';
UPDATE tlkptaxon SET newlabel='Ziziphus spina-christi (L.) Desf.', htmllabel='<i>Ziziphus spina-christi</i> (L.) Desf.', colparentid='648P5' WHERE colid='5D648';
UPDATE tlkptaxon SET newlabel='Afrocarpus falcatus (Thunb.) C. N. Page', htmllabel='<i>Afrocarpus falcatus</i> (Thunb.) C. N. Page', colparentid='Q53' WHERE colid='65FZQ';
UPDATE tlkptaxon SET newlabel='Agathis australis (D. Don) Lindl.', htmllabel='<i>Agathis australis</i> (D. Don) Lindl.', colparentid='QM5' WHERE colid='5TQT6';
UPDATE tlkptaxon SET newlabel='Albizia', htmllabel='<i>Albizia</i>', colparentid='623QT' WHERE colid='RJB';
UPDATE tlkptaxon SET newlabel='Alnus glutinosa (L.) Gaertn.', htmllabel='<i>Alnus glutinosa</i> (L.) Gaertn.', colparentid='SQX' WHERE colid='C2Q2';
UPDATE tlkptaxon SET newlabel='Bauhinia', htmllabel='<i>Bauhinia</i>', colparentid='623QT' WHERE colid='38XT';
UPDATE tlkptaxon SET newlabel='Burkea', htmllabel='<i>Burkea</i>', colparentid='623QT' WHERE colid='62HQ5';
UPDATE tlkptaxon SET newlabel='Chamaecyparis obtusa (Siebold & Zucc.) Endl.', htmllabel='<i>Chamaecyparis obtusa</i> (Siebold & Zucc.) Endl.', colparentid='3LZ8' WHERE colid='TJJH';
UPDATE tlkptaxon SET newlabel='Combretaceae R. Br.', htmllabel='Combretaceae R. Br.', colparentid='3LY' WHERE colid='8GM';
UPDATE tlkptaxon SET newlabel='Cupressus dupreziana var. atlantica (Gaussen) Silba', htmllabel='<i>Cupressus dupreziana</i> var. <i>atlantica</i> (Gaussen) Silba', colparentid='32FWG' WHERE colid='5NDLK';
UPDATE tlkptaxon SET newlabel='Dacrycarpus dacrydioides (A. Rich.) de Laub.', htmllabel='<i>Dacrycarpus dacrydioides</i> (A. Rich.) de Laub.', colparentid='3Z9D' WHERE colid='33T93';
UPDATE tlkptaxon SET newlabel='Daniellia', htmllabel='<i>Daniellia</i>', colparentid='623QT' WHERE colid='3ZR7';
UPDATE tlkptaxon SET newlabel='Ericaceae Juss.', htmllabel='Ericaceae Juss.', colparentid='625QY' WHERE colid='623P8';
UPDATE tlkptaxon SET newlabel='Eucalyptus camaldulensis Dehnh.', htmllabel='<i>Eucalyptus camaldulensis</i> Dehnh.', colparentid='4F2P' WHERE colid='3BPQQ';
UPDATE tlkptaxon SET newlabel='Fagus crenata Blume', htmllabel='<i>Fagus crenata</i> Blume', colparentid='4J87' WHERE colid='6HNYV';
UPDATE tlkptaxon SET newlabel='Faidherbia', htmllabel='<i>Faidherbia</i>', colparentid='623QT' WHERE colid='4J8B';
UPDATE tlkptaxon SET newlabel='Fraxinus pennsylvanica Marshall', htmllabel='<i>Fraxinus pennsylvanica</i> Marshall', colparentid='4L2N' WHERE colid='6JMHT';
UPDATE tlkptaxon SET newlabel='Halocarpus bidwillii (Hook. f. ex Kirk) Quinn', htmllabel='<i>Halocarpus bidwillii</i> (Hook. f. ex Kirk) Quinn', colparentid='4SVB' WHERE colid='6KZVH';
UPDATE tlkptaxon SET newlabel='Isoberlinia', htmllabel='<i>Isoberlinia</i>', colparentid='623QT' WHERE colid='55DT';
UPDATE tlkptaxon SET newlabel='Juniperus procera Hochst. ex Endl.', htmllabel='<i>Juniperus procera</i> Hochst. ex Endl.', colparentid='56YL' WHERE colid='6NFZX';
UPDATE tlkptaxon SET newlabel='Laburnum anagyroides Medik.', htmllabel='<i>Laburnum anagyroides</i> Medik.', colparentid='59QH' WHERE colid='3RL8J';
UPDATE tlkptaxon SET newlabel='Lannea A. Rich.', htmllabel='<i>Lannea</i> A. Rich.', colparentid='KCW' WHERE colid='62XM8';
UPDATE tlkptaxon SET newlabel='Lepidothamnus intermedius (Kirk) Quinn', htmllabel='<i>Lepidothamnus intermedius</i> (Kirk) Quinn', colparentid='5CLL' WHERE colid='3TDSS';
UPDATE tlkptaxon SET newlabel='Magnolia champaca (L.) Baill. ex Pierre', htmllabel='<i>Magnolia champaca</i> (L.) Baill. ex Pierre', colparentid='5K5W' WHERE colid='3XH7J';
UPDATE tlkptaxon SET newlabel='Malvaceae Juss.', htmllabel='Malvaceae Juss.', colparentid='3HP' WHERE colid='CDB';
UPDATE tlkptaxon SET newlabel='Mitragyna', htmllabel='<i>Mitragyna</i>', colparentid='6278P' WHERE colid='63BT8';
UPDATE tlkptaxon SET newlabel='Nyssa ogeche W.Bartram ex Marshall', htmllabel='<i>Nyssa ogeche</i> W.Bartram ex Marshall', colparentid='665H' WHERE colid='487MG';
UPDATE tlkptaxon SET newlabel='Ostrya carpinifolia Scop.', htmllabel='<i>Ostrya carpinifolia</i> Scop.', colparentid='63H3Q' WHERE colid='6TCXD';
UPDATE tlkptaxon SET newlabel='Paulownia tomentosa (Thunb.) Steud.', htmllabel='<i>Paulownia tomentosa</i> (Thunb.) Steud.', colparentid='6JPP' WHERE colid='6TVD7';
UPDATE tlkptaxon SET newlabel='Pericopsis', htmllabel='<i>Pericopsis</i>', colparentid='623QT' WHERE colid='6L84';
UPDATE tlkptaxon SET newlabel='Pinus ayacahuite Ehrenb. ex Schltdl.', htmllabel='<i>Pinus ayacahuite</i> Ehrenb. ex Schltdl.', colparentid='6QPY' WHERE colid='4J22X';
UPDATE tlkptaxon SET newlabel='Pinus durangensis Martínez', htmllabel='<i>Pinus durangensis</i> Martínez', colparentid='6QPY' WHERE colid='4J266';
UPDATE tlkptaxon SET newlabel='Pinus lumholtzii B.L. Rob & Fernald', htmllabel='<i>Pinus lumholtzii</i> B.L. Rob & Fernald', colparentid='6QPY' WHERE colid='4J2B2';
UPDATE tlkptaxon SET newlabel='Pinus tabuliformis Carrière', htmllabel='<i>Pinus tabuliformis</i> Carrière', colparentid='6QPY' WHERE colid='4J2J7';
UPDATE tlkptaxon SET newlabel='Pinus teocote Schiede ex Schltdl. & Cham.', htmllabel='<i>Pinus teocote</i> Schiede ex Schltdl. & Cham.', colparentid='6QPY' WHERE colid='4J2JP';
UPDATE tlkptaxon SET newlabel='Platonia insignis Mart.', htmllabel='<i>Platonia insignis</i> Mart.', colparentid='6RML' WHERE colid='6VPND';
UPDATE tlkptaxon SET newlabel='Populus balsamifera subsp. trichocarpa (Torr. & A. Gray) Brayshaw', htmllabel='<i>Populus balsamifera</i> subsp. <i>trichocarpa</i> (Torr. & A. Gray) Brayshaw', colparentid='4LVJS' WHERE colid='5KLW2';
UPDATE tlkptaxon SET newlabel='Prosopis africana (Guill. & Perr.) Taub.', htmllabel='<i>Prosopis africana</i> (Guill. & Perr.) Taub.', colparentid='6X78' WHERE colid='4MW5Q';
UPDATE tlkptaxon SET newlabel='Pterocarpus erinaceus Poir.', htmllabel='<i>Pterocarpus erinaceus</i> Poir.', colparentid='74N7' WHERE colid='4PVHJ';
UPDATE tlkptaxon SET newlabel='Quercus coccinea Münchh.', htmllabel='<i>Quercus coccinea</i> Münchh.', colparentid='76HN' WHERE colid='6X435';
UPDATE tlkptaxon SET newlabel='Quercus virginiana Mill.', htmllabel='<i>Quercus virginiana</i> Mill.', colparentid='76HN' WHERE colid='7944S';
UPDATE tlkptaxon SET newlabel='Corymbia nesophila (Blakely) K. D. Hill & L. A. S. Johnson', htmllabel='<i>Corymbia nesophila</i> (Blakely) K. D. Hill & L. A. S. Johnson', colparentid='62NTZ' WHERE colid='6B6VZ';
UPDATE tlkptaxon SET newlabel='Dipterocarpaceae Blume', htmllabel='Dipterocarpaceae Blume', colparentid='3HP' WHERE colid='9CP';
UPDATE tlkptaxon SET newlabel='Mitragyna inermis (Willd.) Kuntze', htmllabel='<i>Mitragyna inermis</i> (Willd.) Kuntze', colparentid='63BT8' WHERE colid='43QM6';
UPDATE tlkptaxon SET newlabel='Pinus aristata Engelm.', htmllabel='<i>Pinus aristata</i> Engelm.', colparentid='6QPY' WHERE colid='4J22N';
UPDATE tlkptaxon SET newlabel='Pinus monophylla Torr. & Frém.', htmllabel='<i>Pinus monophylla</i> Torr. & Frém.', colparentid='6QPY' WHERE colid='4J2C7';
UPDATE tlkptaxon SET newlabel='Pinus quadrifolia Parl. ex Sudw.', htmllabel='<i>Pinus quadrifolia</i> Parl. ex Sudw.', colparentid='6QPY' WHERE colid='4J2FN';
UPDATE tlkptaxon SET newlabel='Quercus garryana Douglas ex Hook.', htmllabel='<i>Quercus garryana</i> Douglas ex Hook.', colparentid='76HN' WHERE colid='6WRQZ';
UPDATE tlkptaxon SET newlabel='Quercus hypoleucoides A.Camus', htmllabel='<i>Quercus hypoleucoides</i> A.Camus', colparentid='76HN' WHERE colid='4R56Y';
UPDATE tlkptaxon SET newlabel='Quercus mongolica Fisch. ex Ledeb.', htmllabel='<i>Quercus mongolica</i> Fisch. ex Ledeb.', colparentid='76HN' WHERE colid='4R5KF';
UPDATE tlkptaxon SET newlabel='Rhamnus cathartica L.', htmllabel='<i>Rhamnus cathartica</i> L.', colparentid='78CD' WHERE colid='4RZYQ';
UPDATE tlkptaxon SET newlabel='Salix arbusculoides Andersson', htmllabel='<i>Salix arbusculoides</i> Andersson', colparentid='7BWB' WHERE colid='6XCJ5';
UPDATE tlkptaxon SET newlabel='Salix planifolia Pursh', htmllabel='<i>Salix planifolia</i> Pursh', colparentid='7BWB' WHERE colid='79DSK';
UPDATE tlkptaxon SET newlabel='Saxegothaea conspicua Lindl.', htmllabel='<i>Saxegothaea conspicua</i> Lindl.', colparentid='7CV2' WHERE colid='79QZ2';
UPDATE tlkptaxon SET newlabel='Sequoiadendron giganteum (Lindl.) J.T. Buchholz', htmllabel='<i>Sequoiadendron giganteum</i> (Lindl.) J.T. Buchholz', colparentid='7FQF' WHERE colid='4WSQK';
UPDATE tlkptaxon SET newlabel='Tamarindus indica L.', htmllabel='<i>Tamarindus indica</i> L.', colparentid='7S9Q' WHERE colid='54M92';
UPDATE tlkptaxon SET newlabel='Taxodium distichum var. imbricarium (Nutt.) Sarg.', htmllabel='<i>Taxodium distichum</i> var. <i>imbricarium</i> (Nutt.) Sarg.', colparentid='5526G' WHERE colid='7N2BM';
UPDATE tlkptaxon SET newlabel='Thujopsis dolabrata (Thunb. ex L. f.) Siebold & Zucc.', htmllabel='<i>Thujopsis dolabrata</i> (Thunb. ex L. f.) Siebold & Zucc.', colparentid='63WD4' WHERE colid='7C9LX';
UPDATE tlkptaxon SET newlabel='Tsuga sieboldii Carrière', htmllabel='<i>Tsuga sieboldii</i> Carrière', colparentid='6486F' WHERE colid='59HMW';
UPDATE tlkptaxon SET newlabel='Vachellia nilotica (L.) P.J.H.Hurter & Mabb.', htmllabel='<i>Vachellia nilotica</i> (L.) P.J.H.Hurter & Mabb.', colparentid='85LM' WHERE colid='7F8VF';
UPDATE tlkptaxon SET newlabel='Vouacapoua americana Aubl.', htmllabel='<i>Vouacapoua americana</i> Aubl.', colparentid='647Q2' WHERE colid='5BRJV';
UPDATE tlkptaxon SET newlabel='Xanthocyparis nootkatensis (D. Don) Farjon & Harder', htmllabel='<i>Xanthocyparis nootkatensis</i> (D. Don) Farjon & Harder', colparentid='649GY' WHERE colid='5C5GD';
UPDATE tlkptaxon SET newlabel='Abies', htmllabel='<i>Abies</i>', colparentid='625M7' WHERE colid='627WF';
UPDATE tlkptaxon SET newlabel='Abies alba Mill.', htmllabel='<i>Abies alba</i> Mill.', colparentid='627WF' WHERE colid='8K9Y';
UPDATE tlkptaxon SET newlabel='Abies amabilis Douglas ex J. Forbes', htmllabel='<i>Abies amabilis</i> Douglas ex J. Forbes', colparentid='627WF' WHERE colid='63Z5D';
UPDATE tlkptaxon SET newlabel='Abies balsamea (L.) Mill.', htmllabel='<i>Abies balsamea</i> (L.) Mill.', colparentid='627WF' WHERE colid='63Z6Q';
UPDATE tlkptaxon SET newlabel='Abies borisii-regis Mattf.', htmllabel='<i>Abies borisii-regis</i> Mattf.', colparentid='627WF' WHERE colid='63Z74';
UPDATE tlkptaxon SET newlabel='Abies bracteata (D. Don) A. Poit.', htmllabel='<i>Abies bracteata</i> (D. Don) A. Poit.', colparentid='627WF' WHERE colid='63YV6';
UPDATE tlkptaxon SET newlabel='Abies cephalonica Loudon', htmllabel='<i>Abies cephalonica</i> Loudon', colparentid='627WF' WHERE colid='8KB8';
UPDATE tlkptaxon SET newlabel='Abies cilicica (Antoine & Kotschy) Carrière', htmllabel='<i>Abies cilicica</i> (Antoine & Kotschy) Carrière', colparentid='627WF' WHERE colid='63YV8';
UPDATE tlkptaxon SET newlabel='Abies concolor (Gordon) Lindl. ex Hildebr.', htmllabel='<i>Abies concolor</i> (Gordon) Lindl. ex Hildebr.', colparentid='627WF' WHERE colid='8KBM';
UPDATE tlkptaxon SET newlabel='Abies fargesii Franch.', htmllabel='<i>Abies fargesii</i> Franch.', colparentid='627WF' WHERE colid='8KCF';
UPDATE tlkptaxon SET newlabel='Abies firma Siebold & Zucc.', htmllabel='<i>Abies firma</i> Siebold & Zucc.', colparentid='627WF' WHERE colid='8KCJ';
UPDATE tlkptaxon SET newlabel='Abies nordmanniana (Steven) Spach', htmllabel='<i>Abies nordmanniana</i> (Steven) Spach', colparentid='627WF' WHERE colid='63Z83';
UPDATE tlkptaxon SET newlabel='Abies nordmanniana subsp. equi-trojani (Asch. & Sint. ex Boiss.) Coode & Cullen', htmllabel='<i>Abies nordmanniana</i> subsp. <i>equi</i> -trojani (Asch. & Sint. ex Boiss.) Coode & Cullen', colparentid='63Z83' WHERE colid='5FBWL';
UPDATE tlkptaxon SET newlabel='Pinaceae', htmllabel='Pinaceae', colparentid='623FD' WHERE colid='625M7';
UPDATE tlkptaxon SET newlabel='Moraceae Gaudin', htmllabel='Moraceae Gaudin', colparentid='3Z6' WHERE colid='CXM';
UPDATE tlkptaxon SET newlabel='Salix caprea L.', htmllabel='<i>Salix caprea</i> L.', colparentid='7BWB' WHERE colid='6XCBJ';
UPDATE tlkptaxon SET newlabel='Abies hickelii Flous & Gaussen', htmllabel='<i>Abies hickelii</i> Flous & Gaussen', colparentid='627WF' WHERE colid='63YTX';
UPDATE tlkptaxon SET newlabel='Albizia gummifera (J.F.Gmel.) C.A.Sm.', htmllabel='<i>Albizia gummifera</i> (J.F.Gmel.) C.A.Sm.', colparentid='RJB' WHERE colid='BG6W';
UPDATE tlkptaxon SET newlabel='Cannabaceae Martinov', htmllabel='Cannabaceae Martinov', colparentid='3Z6' WHERE colid='7NV';
UPDATE tlkptaxon SET newlabel='Cassia', htmllabel='<i>Cassia</i>', colparentid='623QT' WHERE colid='3J3X';
UPDATE tlkptaxon SET newlabel='Casuarina L.', htmllabel='<i>Casuarina</i> L.', colparentid='7SM' WHERE colid='3J7F';
UPDATE tlkptaxon SET newlabel='Combretum collinum Fresen.', htmllabel='<i>Combretum collinum</i> Fresen.', colparentid='3SRG' WHERE colid='5ZPHM';
UPDATE tlkptaxon SET newlabel='Combretum collinum subsp. hypopilinum (Diels) Okafor', htmllabel='<i>Combretum collinum</i> subsp. <i>hypopilinum</i> (Diels) Okafor', colparentid='5ZPHM' WHERE colid='5GKJ3';
UPDATE tlkptaxon SET newlabel='Cordia goetzei Gürke', htmllabel='<i>Cordia goetzei</i> Gürke', colparentid='62MK7' WHERE colid='6B2HB';
UPDATE tlkptaxon SET newlabel='Dichrostachys', htmllabel='<i>Dichrostachys</i>', colparentid='623QT' WHERE colid='44Q7';
UPDATE tlkptaxon SET newlabel='Dichrostachys cinerea (L.) Wight & Arn.', htmllabel='<i>Dichrostachys cinerea</i> (L.) Wight & Arn.', colparentid='44Q7' WHERE colid='35PD3';
UPDATE tlkptaxon SET newlabel='Euclea', htmllabel='<i>Euclea</i>', colparentid='9JQ' WHERE colid='4F8D';
UPDATE tlkptaxon SET newlabel='Vachellia seyal (Delile) P.J.H.Hurter', htmllabel='<i>Vachellia seyal</i> (Delile) P.J.H.Hurter', colparentid='85LM' WHERE colid='7F8YD';
UPDATE tlkptaxon SET newlabel='Euclea natalensis A.DC.', htmllabel='<i>Euclea natalensis</i> A.DC.', colparentid='4F8D' WHERE colid='6GTV8';
UPDATE tlkptaxon SET newlabel='Garcinia L.', htmllabel='<i>Garcinia</i> L.', colparentid='JCN' WHERE colid='4M7G';
UPDATE tlkptaxon SET newlabel='Garcinia livingstonei T. Anderson', htmllabel='<i>Garcinia livingstonei</i> T. Anderson', colparentid='4M7G' WHERE colid='3F9Y9';
UPDATE tlkptaxon SET newlabel='Handroanthus Mattos', htmllabel='<i>Handroanthus</i> Mattos', colparentid='77P' WHERE colid='4T83';
UPDATE tlkptaxon SET newlabel='Handroanthus impetiginosum (Mart. ex DC.) Mattos', htmllabel='<i>Handroanthus impetiginosum</i> (Mart. ex DC.) Mattos', colparentid='4T83' WHERE colid='3JJRM';
UPDATE tlkptaxon SET newlabel='Khaya A. Juss.', htmllabel='<i>Khaya</i> A. Juss.', colparentid='626TL' WHERE colid='5864';
UPDATE tlkptaxon SET newlabel='Khaya senegalensis (Desv.) A. Juss.', htmllabel='<i>Khaya senegalensis</i> (Desv.) A. Juss.', colparentid='5864' WHERE colid='3R6XS';
UPDATE tlkptaxon SET newlabel='Lannea velutina A. Rich.', htmllabel='<i>Lannea velutina</i> A. Rich.', colparentid='62XM8' WHERE colid='3S7H4';
UPDATE tlkptaxon SET newlabel='Lepisanthes Blume', htmllabel='<i>Lepisanthes</i> Blume', colparentid='628N6' WHERE colid='5CMX';
UPDATE tlkptaxon SET newlabel='Lepisanthes senegalensis (Poir.) Leenh.', htmllabel='<i>Lepisanthes senegalensis</i> (Poir.) Leenh.', colparentid='5CMX' WHERE colid='3TFD5';
UPDATE tlkptaxon SET newlabel='Mimusops', htmllabel='<i>Mimusops</i>', colparentid='FY4' WHERE colid='5T9G';
UPDATE tlkptaxon SET newlabel='Mimusops obtusifolia Lam.', htmllabel='<i>Mimusops obtusifolia</i> Lam.', colparentid='5T9G' WHERE colid='43HYQ';
UPDATE tlkptaxon SET newlabel='Newtonia', htmllabel='<i>Newtonia</i>', colparentid='623QT' WHERE colid='63F3M';
UPDATE tlkptaxon SET newlabel='Newtonia hildebrandtii (Vatke) Torre', htmllabel='<i>Newtonia hildebrandtii</i> (Vatke) Torre', colparentid='63F3M' WHERE colid='47BYZ';
UPDATE tlkptaxon SET newlabel='Pinus pseudostrobus Lindl.', htmllabel='<i>Pinus pseudostrobus</i> Lindl.', colparentid='6QPY' WHERE colid='4J2FG';
UPDATE tlkptaxon SET newlabel='Polysphaeria', htmllabel='<i>Polysphaeria</i>', colparentid='6278P' WHERE colid='63P5N';
UPDATE tlkptaxon SET newlabel='Polysphaeria multiflora Hiern', htmllabel='<i>Polysphaeria multiflora</i> Hiern', colparentid='63P5N' WHERE colid='4LLZJ';
UPDATE tlkptaxon SET newlabel='Populus ilicifolia (Engl.) Rouleau', htmllabel='<i>Populus ilicifolia</i> (Engl.) Rouleau', colparentid='63PVP' WHERE colid='4LVM8';
UPDATE tlkptaxon SET newlabel='Rinorea Aubl.', htmllabel='<i>Rinorea</i> Aubl.', colparentid='KHG' WHERE colid='63SB8';
UPDATE tlkptaxon SET newlabel='Rinorea elliptica (Oliv.) Kuntze', htmllabel='<i>Rinorea elliptica</i> (Oliv.) Kuntze', colparentid='63SB8' WHERE colid='4T49K';
UPDATE tlkptaxon SET newlabel='Sapindaceae Juss.', htmllabel='Sapindaceae Juss.', colparentid='3ZY' WHERE colid='FY3';
UPDATE tlkptaxon SET newlabel='Senegalia polyacantha (Willd.) Seigler & Ebinger', htmllabel='<i>Senegalia polyacantha</i> (Willd.) Seigler & Ebinger', colparentid='63SM7' WHERE colid='4WP3C';
UPDATE tlkptaxon SET newlabel='Spirostachys', htmllabel='<i>Spirostachys</i>', colparentid='9Y7' WHERE colid='6466Y';
UPDATE tlkptaxon SET newlabel='Spirostachys venenifera (Pax) Pax', htmllabel='<i>Spirostachys venenifera</i> (Pax) Pax', colparentid='6466Y' WHERE colid='4Z8N4';
UPDATE tlkptaxon SET newlabel='Sterculia appendiculata K. Schum. ex Engl.', htmllabel='<i>Sterculia appendiculata</i> K. Schum. ex Engl.', colparentid='7N5T' WHERE colid='52D9H';
UPDATE tlkptaxon SET newlabel='Tabebuia Gomes', htmllabel='<i>Tabebuia</i> Gomes', colparentid='77P' WHERE colid='7RQF';
UPDATE tlkptaxon SET newlabel='Terminalia brevipes Pamp.', htmllabel='<i>Terminalia brevipes</i> Pamp.', colparentid='647GM' WHERE colid='55G35';
UPDATE tlkptaxon SET newlabel='Trema Lour.', htmllabel='<i>Trema</i> Lour.', colparentid='7NV' WHERE colid='7YPK';
UPDATE tlkptaxon SET newlabel='Trichilia P. Browne', htmllabel='<i>Trichilia</i> P. Browne', colparentid='JVZ' WHERE colid='7Z63';
UPDATE tlkptaxon SET newlabel='Trichilia emetica (Forssk.) Vahl', htmllabel='<i>Trichilia emetica</i> (Forssk.) Vahl', colparentid='7Z63' WHERE colid='588Q2';
UPDATE tlkptaxon SET newlabel='Violaceae Batsch', htmllabel='Violaceae Batsch', colparentid='MLP' WHERE colid='6272Z';
UPDATE tlkptaxon SET newlabel='Vachellia', htmllabel='<i>Vachellia</i>', colparentid='623QT' WHERE colid='85LM';
UPDATE tlkptaxon SET newlabel='Vachellia elatior (Brenan) Kyal. & Boatwr.', htmllabel='<i>Vachellia elatior</i> (Brenan) Kyal. & Boatwr.', colparentid='85LM' WHERE colid='7F98J';
UPDATE tlkptaxon SET newlabel='Vachellia robusta (Burch.) Kyal. & Boatwr.', htmllabel='<i>Vachellia robusta</i> (Burch.) Kyal. & Boatwr.', colparentid='85LM' WHERE colid='7F8XX';

-- Fix some rank changes
UPDATE tlkptaxon SET taxonrankid=10 WHERE colid='4RZZQ';
UPDATE tlkptaxon SET taxonrankid=10 WHERE colid='LPCX';
UPDATE tlkptaxon SET taxonrankid=10 WHERE colid='C2TP';
UPDATE tlkptaxon SET taxonrankid=10 WHERE colid='LP4H';
UPDATE tlkptaxon SET taxonrankid=10 WHERE colid='LPBT';
UPDATE tlkptaxon SET taxonrankid=12 WHERE colid='5PNDP';
UPDATE tlkptaxon SET taxonrankid=9 WHERE colid='4J2KB';
UPDATE tlkptaxon SET taxonrankid=10 WHERE colid='5FXVF';


-- Make sure new colid is unique
ALTER TABLE tlkptaxon ADD CONSTRAINT "uniq_colid" UNIQUE (colid);

-- Fix element table to include new colid
DROP TRIGGER update_element_rebuildmetacache ON tblelement;
ALTER TABLE tblelement ADD COLUMN colid VARCHAR;
UPDATE tblelement SET colid = tlkptaxon.colid FROM tlkptaxon WHERE tblelement.taxonid=tlkptaxon.taxonid;
CREATE TRIGGER update_element_rebuildmetacache AFTER INSERT OR UPDATE ON tblelement FOR EACH ROW EXECUTE PROCEDURE cpgdb.rebuildmetacacheforelement();

ALTER TABLE tlkptaxon DROP CONSTRAINT "pkey_taxon";
DROP VIEW vwtblelement;
DROP VIEW vwtlkptaxon;
DROP VIEW IF EXISTS vwportalcomb; 

DROP VIEW IF EXISTS vwportaldata1;
DROP VIEW IF EXISTS vwportaldata2;




ALTER TABLE tlkptaxon DROP COLUMN taxonid;
ALTER TABLE tlkptaxon ADD COLUMN taxonid varchar;
UPDATE tlkptaxon SET taxonid=colid;
ALTER TABLE tlkptaxon ADD CONSTRAINT "pkey_taxon" PRIMARY KEY (taxonid);
ALTER TABLE tlkptaxon DROP COLUMN parenttaxonid;
ALTER TABLE tlkptaxon ADD COLUMN parenttaxonid VARCHAR;
UPDATE tlkptaxon SET parenttaxonid=colparentid;
UPDATE tlkptaxon SET label=newlabel;
ALTER TABLE tlkptaxon DROP COLUMN newlabel;

CREATE VIEW vwtlkptaxon AS 
SELECT taxon.taxonid, 
taxon.label AS taxonlabel,
taxon.parenttaxonid,
taxon.colid,
taxon.colparentid,
rank.taxonrank
FROM tlkptaxon taxon
JOIN tlkptaxonrank rank ON rank.taxonrankid = taxon.taxonrankid;


DROP VIEW vw_tlotoradius;
DROP VIEW vw_elementtoradius;

ALTER TABLE tblelement DROP COLUMN taxonid;
ALTER TABLE tblelement RENAME COLUMN colid TO taxonid;
ALTER TABLE public.tblelement ADD CONSTRAINT fkey_element_taxon FOREIGN KEY (taxonid) REFERENCES public.tlkptaxon (taxonid) ON UPDATE NO ACTION ON DELETE NO ACTION;




CREATE VIEW vwtblelement AS 
 SELECT ( SELECT findobjecttoplevelancestor.code
           FROM cpgdb.findobjecttoplevelancestor(e.objectid) findobjecttoplevelancestor(objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, locationcomment, creator, owner, parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry, locationcountry_1, vegetationtype, domainid, projectid)) AS objectcode,
    e.comments,
    dom.domainid,
    dom.domain,
    e.elementid,
    e.locationprecision,
    e.code AS title,
    e.code,
    e.createdtimestamp,
    e.lastmodifiedtimestamp,
    e.locationgeometry,
    ( SELECT st_asgml(3, e.locationgeometry, 15, 1) AS st_asgml) AS gml,
    xmin(e.locationgeometry::box3d) AS longitude,
    ymin(e.locationgeometry::box3d) AS latitude,
    e.islivetree,
    e.originaltaxonname,
    e.locationtypeid,
    e.locationcomment,
    e.locationaddressline1,
    e.locationaddressline2,
    e.locationcityortown,
    e.locationstateprovinceregion,
    e.locationpostalcode,
    e.locationcountry,
    array_to_string(e.file, '><'::text) AS file,
    e.description,
    e.processing,
    e.marks,
    e.diameter,
    e.width,
    e.height,
    e.depth,
    e.unsupportedxml,
    e.objectid,
    e.elementtypeid,
    e.authenticity,
    e.elementshapeid,
    shape.elementshape,
    tbltype.elementtype,
    loctype.locationtype,
    e.altitude,
    e.slopeangle,
    e.slopeazimuth,
    e.soildescription,
    e.soildepth,
    e.bedrockdescription,
    vwt.taxonid,
    vwt.taxonlabel,
    vwt.parenttaxonid,
    vwt.colid,
    vwt.colparentid,
    vwt.taxonrank,
    unit.unit AS units,
    unit.unitid
   FROM tblelement e
     LEFT JOIN tlkpdomain dom ON e.domainid = dom.domainid
     LEFT JOIN tlkpelementshape shape ON e.elementshapeid = shape.elementshapeid
     LEFT JOIN tlkpelementtype tbltype ON e.elementtypeid = tbltype.elementtypeid
     LEFT JOIN tlkplocationtype loctype ON e.locationtypeid = loctype.locationtypeid
     LEFT JOIN tlkpunit unit ON e.units = unit.unitid
     LEFT JOIN vwtlkptaxon vwt ON e.taxonid = vwt.taxonid;



CREATE VIEW vw_elementtoradius AS 
SELECT cpgdb.tloid(e.*) AS tlo_objectid,
    e.elementid AS e_elementid,
    e.taxonid AS e_taxonid,
    e.locationprecision AS e_locationprecision,
    e.code AS e_code,
    e.createdtimestamp AS e_createdtimestamp,
    e.lastmodifiedtimestamp AS e_lastmodifiedtimestamp,
    e.locationgeometry AS e_locationgeometry,
    e.islivetree AS e_islivetree,
    e.originaltaxonname AS e_originaltaxonname,
    e.locationtypeid AS e_locationtypeid,
    e.locationcomment AS e_locationcomment,
    e.file AS e_file,
    e.description AS e_description,
    e.processing AS e_processing,
    e.marks AS e_marks,
    e.diameter AS e_diameter,
    e.width AS e_width,
    e.height AS e_height,
    e.depth AS e_depth,
    e.unsupportedxml AS e_unsupportedxml,
    e.unitsold AS e_units,
    e.elementtypeid AS e_elementtypeid,
    e.authenticity AS e_authenticity,
    e.elementshapeid AS e_elementshapeid,
    e.altitudeint AS e_altitudeint,
    e.slopeangle AS e_slopeangle,
    e.slopeazimuth AS e_slopeazimuth,
    e.soildescription AS e_soildescription,
    e.soildepth AS e_soildepth,
    e.bedrockdescription AS e_bedrockdescription,
    e.comments AS e_comments,
    e.locationaddressline2 AS e_locationaddressline2,
    e.locationcityortown AS e_locationcityortown,
    e.locationstateprovinceregion AS e_locationstateprovinceregion,
    e.locationpostalcode AS e_locationpostalcode,
    e.locationcountry AS e_locationcountry,
    e.locationaddressline1 AS e_locationaddressline1,
    e.altitude AS e_altitude,
    e.gispkey AS e_gispkey,
    s.sampleid AS s_sampleid,
    s.code AS s_code,
    s.samplingdate AS s_samplingdate,
    s.createdtimestamp AS s_createdtimestamp,
    s.lastmodifiedtimestamp AS s_lastmodifiedtimestamp,
    s.type AS s_type,
    s.identifierdomain AS s_identifierdomain,
    s.file AS s_file,
    s."position" AS s_position,
    s.state AS s_state,
    s.knots AS s_knots,
    s.description AS s_description,
    s.datecertaintyid AS s_datecertaintyid,
    s.typeid AS s_typeid,
    s.boxid AS s_boxid,
    s.comments AS s_comments,
    r.radiusid AS r_radiusid,
    r.code AS r_code,
    r.createdtimestamp AS r_createdtimestamp,
    r.lastmodifiedtimestamp AS r_lastmodifiedtimestamp,
    r.numberofsapwoodrings AS r_numberofsapwoodrings,
    r.pithid AS r_pithid,
    r.barkpresent AS r_barkpresent,
    r.lastringunderbark AS r_lastringunderbark,
    r.missingheartwoodringstopith AS r_missingheartwoodringstopith,
    r.missingheartwoodringstopithfoundation AS r_missingheartwoodringstopithfoundation,
    r.missingsapwoodringstobark AS r_missingsapwoodringstobark,
    r.missingsapwoodringstobarkfoundation AS r_missingsapwoodringstobarkfoundation,
    r.sapwoodid AS r_sapwoodid,
    r.heartwoodid AS r_heartwoodid,
    r.azimuth AS r_azimuth,
    r.comments AS r_comments,
    r.lastringunderbarkpresent AS r_lastringunderbarkpresent,
    r.nrofunmeasuredinnerrings AS r_nrofunmeasuredinnerrings,
    r.nrofunmeasuredouterrings AS r_nrofunmeasuredouterrings
   FROM tblelement e
LEFT JOIN tblsample s ON e.elementid = s.elementid
LEFT JOIN tblradius r ON s.sampleid = r.sampleid;



CREATE VIEW vw_tlotoradius AS 
SELECT tlo.title AS tlo_title,
    tlo.code AS tlo_code,
    tlo.createdtimestamp AS tlo_createdtimestamp,
    tlo.lastmodifiedtimestamp AS tlo_lastmodifiedtimestamp,
    tlo.locationgeometry AS tlo_locationgeometry,
    tlo.locationtypeid AS tlo_locationtypeid,
    tlo.locationprecision AS tlo_locationprecision,
    tlo.locationcomment AS tlo_locationcomment,
    tlo.file AS tlo_file,
    tlo.creator AS tlo_creator,
    tlo.owner AS tlo_owner,
    tlo.parentobjectid AS tlo_parentobjectid,
    tlo.description AS tlo_description,
    tlo.objecttypeid AS tlo_objecttypeid,
    tlo.coveragetemporalid AS tlo_coveragetemporalid,
    tlo.coveragetemporalfoundationid AS tlo_coveragetemporalfoundationid,
    tlo.comments AS tlo_comments,
    tlo.coveragetemporal AS tlo_coveragetemporal,
    tlo.coveragetemporalfoundation AS tlo_coveragetemporalfoundation,
    tlo.locationaddressline1 AS tlo_locationaddressline1,
    tlo.locationaddressline2 AS tlo_locationaddressline2,
    tlo.locationcityortown AS tlo_locationcityortown,
    tlo.locationstateprovinceregion AS tlo_locationstateprovinceregion,
    tlo.locationpostalcode AS tlo_locationpostalcode,
    tlo.locationcountry AS tlo_locationcountry,
    others.tlo_objectid,
    others.e_elementid,
    others.e_taxonid,
    others.e_locationprecision,
    others.e_code,
    others.e_createdtimestamp,
    others.e_lastmodifiedtimestamp,
    others.e_locationgeometry,
    others.e_islivetree,
    others.e_originaltaxonname,
    others.e_locationtypeid,
    others.e_locationcomment,
    others.e_file,
    others.e_description,
    others.e_processing,
    others.e_marks,
    others.e_diameter,
    others.e_width,
others.e_height,
    others.e_depth,
    others.e_unsupportedxml,
    others.e_units,
    others.e_elementtypeid,
    others.e_authenticity,
    others.e_elementshapeid,
    others.e_altitudeint,
    others.e_slopeangle,
    others.e_slopeazimuth,
    others.e_soildescription,
    others.e_soildepth,
    others.e_bedrockdescription,
    others.e_comments,
    others.e_locationaddressline2,
    others.e_locationcityortown,
    others.e_locationstateprovinceregion,
    others.e_locationpostalcode,
    others.e_locationcountry,
    others.e_locationaddressline1,
    others.e_altitude,
    others.e_gispkey,
    others.s_sampleid,
    others.s_code,
    others.s_samplingdate,
    others.s_createdtimestamp,
    others.s_lastmodifiedtimestamp,
    others.s_type,
    others.s_identifierdomain,
    others.s_file,
    others.s_position,
    others.s_state,
    others.s_knots,
    others.s_description,
    others.s_datecertaintyid,
    others.s_typeid,
    others.s_boxid,
    others.s_comments,
    others.r_radiusid,
    others.r_code,
    others.r_createdtimestamp,
    others.r_lastmodifiedtimestamp,
   others.r_numberofsapwoodrings,
    others.r_pithid,
    others.r_barkpresent,
    others.r_lastringunderbark,
    others.r_missingheartwoodringstopith,
    others.r_missingheartwoodringstopithfoundation,
    others.r_missingsapwoodringstobark,
    others.r_missingsapwoodringstobarkfoundation,
    others.r_sapwoodid,
    others.r_heartwoodid,
    others.r_azimuth,
    others.r_comments,
    others.r_lastringunderbarkpresent,
    others.r_nrofunmeasuredinnerrings,
    others.r_nrofunmeasuredouterrings
   FROM tblobject tlo
LEFT JOIN vw_elementtoradius others ON others.tlo_objectid = tlo.objectid;


