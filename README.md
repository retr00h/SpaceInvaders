# SpaceInvaders
<h1>Funzionalità principali</h1>

Schermata principale (porta alla schermata delle impostazioni, ad una schermata "tutorial", inizia una partita oppure porta alla schermata dei salvataggi);

<h2>Funzionalità principali di una partita</h2>
Generare ondate di nemici;
Generare powerup casuali ogni tanto (da definire)->(si potrebbero generare casualmente dopo aver eliminato dei nemici oppure dopo un numero prestabilito di nemici);
Muovere il personaggio coi dati dell'accelerometro;
Sparare toccando lo schermo (da definire);
Perdere una vita se il personaggio viene colpito (o guadagnarne fino ad un max di X quando si prende un powerup specifico);
Sistema di punteggio (i powerup possono dare X, Y, Z, ... punti (cambia da powerup a powerup), i nemici danno un numero fisso di punti quando abbattuti);



<h1>Funzionalità secondarie</h1>

Schermata delle impostazioni (permette di cambiare lingua e di attivare/disattivare audio e/o vibrazioni);
Schermata dei salvataggi (mostra una lista di partite esistenti (prendendole da un DB) e permette di giocarle e di cancellarle);
Mettere in pausa una partita o tornare alla schermata principale;
Salvataggio automatico durante l'onPause();




<h1>Idee realizzative</h1>

activity_start è l'activity avviata all'avvio dell'app, conterrà il tasto start, il titolo del gioco, l'icona delle opzioni e magari ci sta anche inserire il punteggio maggiore raggiunto dall'utente

activity_main invece viene lanciata alla pressione del bottone start e contiene il gioco
