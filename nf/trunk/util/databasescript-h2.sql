
insert into usertable ( username, password, firstname, lastname, email, userrole ) values ( 'diego', 'maradona', 'Diego', 'Maradona', 'diego@gmail.com', 'ROLE_ADMIN' );
insert into usertable ( username, password, firstname, lastname, email, userrole ) values ( 'pep', 'guardiola', 'Pep', 'Guardiola', 'pep@gmail.com', 'ROLE_MANAGER' );
insert into usertable ( username, password, firstname, lastname, email, userrole ) values ( 'messi', 'messi', 'Lionel', 'Messi', 'lionel@gmail.com', 'ROLE_MANAGER' );
insert into usertable ( username, password, firstname, lastname, email, userrole ) values ( 'interplay', 'ip4321', 'Interplay', 'Import', 'interplay@interplay-sports.com', 'ROLE_MANAGER' );

insert into category ( name, code, standard, parentid, root, marker ) values ( 'Ferdighet', 'ferdighet', false, null, true, true );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Pasning', 'pasning', false, 1, false, true );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Spillfase', 'spillfase', false, 1, false, true );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Stoptouch', 'stoptouch', false, 2, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Heading', 'heading', true, 2, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Utsidetouch', 'utsidetouch', true, 2, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Innsidetouch', 'innsidetouch', false, 2, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Ferdighetsutvikling', 'ferdighetsutvikling', false, null, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Utdanningsprogram', 'utdanningsprogram', false, null, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Mentalitet', 'mentalitet', false, null, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Idrettsfag', 'idrettsfag', false, null, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Psykologi', 'psykologi', false, null, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Akademi', 'akademi', false, null, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Etablert', 'etablert', true, 3, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Overgang', 'overgang', true, 3, false, false );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Verdens beste spillere', 'verdensbestespillere', false, null, true, true );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Verdens beste relasjoner', 'verdensbesterelasjoner', false, null, true, true );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Verdens beste taktikk', 'verdensbestetaktikk', false, null, true, true );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Verdens beste lag', 'verdensbestelag', false, null, true, true );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Verdens beste trenere', 'verdensbestetrenere', false, null, true, true );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Verdens beste klubber', 'verdensbesteklubber', false, null, true, true );
insert into category ( name, code, standard, parentid, root, marker ) values ( 'Verdens beste nasjoner', 'verdensbestenasjoner', false, null, true, true );

insert into person ( name, code )  values ( 'Lionel Messi', 'lionelmessi' );
insert into person ( name, code )  values ( 'Xavi Hernandez', 'xavihernandez' );
insert into person ( name, code )  values ( 'Gonzalo Higuain', 'gonzalohiguain' );
insert into person ( name, code )  values ( 'Carles Puyol', 'carlespuyol' );
insert into person ( name, code )  values ( 'Sergio Ramos', 'sergioramos' );

insert into team( name, code ) values ( 'Barcelona', 'barcelona' );
insert into team( name, code ) values ( 'Real Madrid', 'realmadrid'  );
insert into team( name, code ) values ( 'Manchester Utd', 'manchesterunited' );
insert into team( name, code ) values ( 'Arsenal', 'arsenal' );

insert into event ( name, code, date, location, awayteam_id, hometeam_id ) values ( 'Real Madrid - Barcelona', 'cAHJqwh01', '2008-06-01', 'Madrid', 2, 1 );
insert into event ( name, code, date, location, awayteam_id, hometeam_id )  values ( 'Barcelona - Manchester Utd', 'cAHJqwh02', '2009-06-01', 'Madrid', 2, 3 );

insert into playlist ( name, code, description, lastupdated, owner_id ) values ( 'Messi highlights', 'pAHJqwh01', 'Messi highlights fra 2008', '2010-05-30', 1 );
insert into playlist ( name, code, description, lastupdated, owner_id ) values ( 'Xavi highlights', 'pAHJqwh02', 'Xavi highlights fra 2008', '2010-05-29', 2 );
insert into playlist ( name, code, description, lastupdated, owner_id ) values ( 'Favoritter', 'pAHJqwh03', 'Favoritter fra 2008', '2010-05-28', 1 );
insert into playlist ( name, code, description, lastupdated, owner_id ) values ( 'Bakrom', 'pAHJqwh04', 'Bakrom', '2010-05-27', 1 );
insert into playlist ( name, code, description, lastupdated, owner_id ) values ( 'Mellomrom', 'pAHJqwh05', 'Mellomrom', '2010-05-26', 1 );
insert into playlist ( name, code, description, lastupdated, owner_id ) values ( 'Scoringer', 'pAHJqwh06', 'Scoringer', '2010-05-25', 1 );
insert into playlist ( name, code, description, lastupdated, owner_id ) values ( 'Forsvar', 'pAHJqwh07', 'Forsvar', '2010-05-24', 1 );
insert into playlist ( name, code, description, lastupdated, owner_id ) values ( 'Angrep', 'pAHJqwh08', 'Angrep', '2010-05-23', 1 );
insert into playlist ( name, code, description, lastupdated, owner_id ) values ( 'Framrom', 'pAHJqwh09', 'Framrom', '2010-05-22', 1 );

insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'REFERENCE', 'cAHJqwh01', 2, 1, 1, 1, 'v001.mp4', '2010-01-01' );
insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'REFERENCE', 'cAHJqwh02', 3, 1, 1, 2, 'v002.mp4', '2010-01-02' );
insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'REFERENCE', 'cAHJqwh03', 4, 2, 1, 3, 'v003.mp4', '2010-01-03' );
insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'REFERENCE', 'cAHJqwh04', 2, 1, 1, 1, 'v004.mp4', '2010-01-04' );
insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'REFERENCE', 'cAHJqwh05', 3, 1, 1, 1, 'v005.mp4', '2010-01-05' );
insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'REFERENCE', 'cAHJqwh06', 4, 1, 1, 1, 'v006.mp4', '2010-01-06' );
insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'REFERENCE', 'cAHJqwh07', 0, 1, 1, 4, 'v007.mp4', '2010-01-07' );
insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'REFERENCE', 'cAHJqwh08', 0, 2, 1, 5, 'v008.mp4', '2010-01-08' );
insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'FEEDBACK', 'cAHJqwh09', 0, 1, 2, 2, 'v009.mp4', '2010-01-09' );
insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'FEEDBACK', 'cAHJqwh10', 0, 1, 2, 2, 'v010.mp4', '2010-01-10' );
insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'FEEDBACK', 'cAHJqwh11', 0, 1, 2, 2, 'v011.mp4', '2010-01-11' );
insert into clip ( type, code, start, team_id, event_id, filename, created ) values ( 'FEEDBACK', 'cAHJqwh12', 0, 1, 2, 2, 'v012.mp4', '2010-01-12' );

insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 1, 4 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 1, 15 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 2, 4 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 2, 15 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 3, 5 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 3, 15 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 4, 6 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 4, 15 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 5, 7 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 5, 15 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 6, 6 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 6, 14 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 7, 5 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 7, 14 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 8, 5 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 8, 14 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 9, 14 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 10, 14 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 11, 14 );
insert into clipcategory ( grade, clip_id, category_id ) values ( 0, 12, 15 );

insert into clip_person ( clip_id, persons_id ) values ( 1, 1 );
insert into clip_person ( clip_id, persons_id ) values ( 2, 2 );
insert into clip_person ( clip_id, persons_id ) values ( 3, 3 );
insert into clip_person ( clip_id, persons_id ) values ( 4, 1 );
insert into clip_person ( clip_id, persons_id ) values ( 5, 1 );
insert into clip_person ( clip_id, persons_id ) values ( 6, 1 );
insert into clip_person ( clip_id, persons_id ) values ( 7, 4 );
insert into clip_person ( clip_id, persons_id ) values ( 8, 5 );
insert into clip_person ( clip_id, persons_id ) values ( 9, 2 );
insert into clip_person ( clip_id, persons_id ) values ( 10, 2 );
insert into clip_person ( clip_id, persons_id ) values ( 11, 2 );
insert into clip_person ( clip_id, persons_id ) values ( 12, 2 );

insert into clip_playlist ( clip_id, playlists_id ) values ( 1, 1 );
insert into clip_playlist ( clip_id, playlists_id ) values ( 4, 1);
insert into clip_playlist ( clip_id, playlists_id ) values ( 5, 1 );
insert into clip_playlist ( clip_id, playlists_id ) values ( 6, 1 );
insert into clip_playlist ( clip_id, playlists_id ) values ( 2, 2 );
insert into clip_playlist ( clip_id, playlists_id ) values ( 10, 2 );
insert into clip_playlist ( clip_id, playlists_id ) values ( 11, 2 );
insert into clip_playlist ( clip_id, playlists_id ) values ( 12, 2 );

insert into playlist_user ( playlist_id, users_id ) values ( 1, 2 );
insert into playlist_user ( playlist_id, users_id ) values ( 2, 1 );

insert into document ( path, title, description, code ) values ( 'd001.pdf', 'Ferdighetsutvikling', 'For å kunne legge til rette for god undervisning, må vi forstå hvilke mekanismer som er involvert i læringsprosessen.', 'BHJqwh01' );
insert into document ( path, title, description, code ) values ( 'd002.pdf', 'Touch-repertoar', 'For å kunne legge til rette for god undervisning, må vi forstå hvilke mekanismer som er involvert i læringsprosessen.', 'BHJqwh02' );
insert into document ( path, title, description, code ) values ( 'd003.pdf', 'Mentalitet', 'For å kunne legge til rette for god undervisning, må vi forstå hvilke mekanismer som er involvert i læringsprosessen.', 'BHJqwh03' );
insert into document ( path, title, description, code ) values ( 'd004.pdf', 'Idrettsfag', 'For å kunne legge til rette for god undervisning, må vi forstå hvilke mekanismer som er involvert i læringsprosessen.', 'BHJqwh04' );

insert into document_category ( document_id, categories_id ) values ( '1', '8' );
insert into document_category ( document_id, categories_id ) values ( '1', '12' );
insert into document_category ( document_id, categories_id ) values ( '2', '4' );
insert into document_category ( document_id, categories_id ) values ( '2', '6' );
insert into document_category ( document_id, categories_id ) values ( '2', '7' );
insert into document_category ( document_id, categories_id ) values ( '3', '10' );
insert into document_category ( document_id, categories_id ) values ( '3', '12' );
insert into document_category ( document_id, categories_id ) values ( '4', '11' );
insert into document_category ( document_id, categories_id ) values ( '4', '13' );
