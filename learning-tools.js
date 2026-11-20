(()=>{
'use strict';
const $=id=>document.getElementById(id);
const esc=s=>String(s??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const phraseGroups=[
{title:'Фурудгоҳ',en:'Airport',items:[['Where is the check-in desk?','Ҷойи бақайдгирӣ дар куҷост?'],['May I see your passport?','Метавонам шиносномаи шуморо бинам?'],['Which gate is my flight?','Парвози ман аз кадом дарвоза аст?'],['My flight has been delayed.','Парвози ман ба таъхир афтодааст.'],['Where can I collect my luggage?','Бағоҷамро аз куҷо гирифта метавонам?'],['Is this the queue for security?','Оё ин навбат барои санҷиши амниятӣ аст?']]},
{title:'Дар мағоза',en:'At the shop',items:[['How much is this?','Ин чанд пул аст?'],['Do you have a larger size?','Андозаи калонтар доред?'],['Can I pay by card?','Метавонам бо корт пардохт кунам?'],['Could I have a receipt, please?','Лутфан, расид дода метавонед?'],['I am just looking, thank you.','Ман фақат тамошо карда истодаам, ташаккур.'],['Can I return this item?','Метавонам ин молро баргардонам?']]},
{title:'Дар кор',en:'At work',items:[['What time does the shift start?','Навбати корӣ соати чанд оғоз мешавад?'],['I have finished the task.','Ман супоришро анҷом додам.'],['Could you explain this again?','Метавонед инро бори дигар фаҳмонед?'],['I will send the report today.','Ман имрӯз ҳисоботро мефиристам.'],['When is the team meeting?','Ҷаласаи гурӯҳ кай мешавад?'],['Thank you for your help.','Барои ёриатон ташаккур.']]},
{title:'Дар беморхона',en:'At the hospital',items:[['I need to see a doctor.','Ман бояд ба духтур муроҷиат кунам.'],['I have a pain here.','Ман дар ин ҷо дард ҳис мекунам.'],['I am allergic to this medicine.','Ман ба ин дору аллергия дорам.'],['Where is the emergency department?','Шуъбаи ёрии таъҷилӣ дар куҷост?'],['I have an appointment today.','Ман имрӯз вақти қабул дорам.'],['Could you call an ambulance?','Метавонед ёрии таъҷилиро даъват кунед?']]},
{title:'Сафар',en:'Travel',items:[['How can I get to the city centre?','Чӣ тавр ба маркази шаҳр равам?'],['How long does the journey take?','Сафар чӣ қадар вақт мегирад?'],['I would like to book a room.','Ман мехоҳам ҳуҷра фармоиш диҳам.'],['Is breakfast included?','Оё наҳорӣ ба нарх дохил аст?'],['Could you show me on the map?','Метавонед дар харита нишон диҳед?'],['I have lost my passport.','Ман шиносномаи худро гум кардам.']]},
{title:'Дар тарабхона',en:'At a restaurant',items:[['Could I see the menu, please?','Лутфан, менюро дидан мумкин аст?'],['I would like some water.','Ман каме об мехоҳам.'],['Does this contain nuts?','Оё дар таркиби ин чормағз ҳаст?'],['Could we have the bill, please?','Лутфан, ҳисобро оварда метавонед?'],['The food was delicious.','Таом хеле болаззат буд.'],['Do you have a vegetarian option?','Таоми гиёҳхорӣ доред?']]},
{title:'Дар меҳмонхона',en:'At a hotel',items:[['I have a reservation.','Ман ҳуҷра фармоиш додаам.'],['Is Wi-Fi available?','Wi-Fi ҳаст?'],['What time is check-out?','Соати баромадан чанд аст?'],['Could I have another towel?','Метавонед боз як дастмол диҳед?'],['The air conditioner is not working.','Кондитсионер кор намекунад.'],['Can I leave my luggage here?','Метавонам бағоҷамро дар ин ҷо гузорам?']]},
{title:'Муоширати ҳаррӯза',en:'Everyday conversation',items:[['Could you speak more slowly?','Метавонед оҳистатар гап занед?'],['I do not understand.','Ман намефаҳмам.'],['Could you repeat that, please?','Лутфан, инро такрор кунед.'],['What does this word mean?','Ин калима чӣ маъно дорад?'],['Nice to meet you.','Аз шиносоӣ бо шумо шодам.'],['Have a good day!','Рӯзи хуб дошта бошед!']]}
];
const exercises=[
['I want to learn English.','Ман мехоҳам забони англисиро омӯзам.'],
['She goes to work every day.','Ӯ ҳар рӯз ба кор меравад.'],
['We are studying together.','Мо якҷоя таҳсил карда истодаем.'],
['They visited the museum yesterday.','Онҳо дирӯз осорхонаро тамошо карданд.'],
['Can you help me, please?','Лутфан, метавонед ба ман ёрӣ диҳед?'],
['My brother is reading a book.','Бародари ман китоб хонда истодааст.'],
['I will call you tomorrow.','Ман пагоҳ ба шумо занг мезанам.'],
['The train arrives at six o’clock.','Қатора соати шаш мерасад.'],
['We have already finished our homework.','Мо аллакай вазифаи хонагиро анҷом додем.'],
['Where did you buy this phone?','Шумо ин телефонро аз куҷо харидед?']
];
const translations=[
['Good morning. How are you?','Субҳ ба хайр. Шумо чӣ хел ҳастед?'],['I would like a glass of water.','Ман як истакон об мехоҳам.'],['Where is the nearest bus stop?','Наздиктарин истгоҳи автобус дар куҷост?'],['I am learning English every day.','Ман ҳар рӯз забони англисиро меомӯзам.'],['Please speak more slowly.','Лутфан, оҳистатар гап занед.'],['How much does this ticket cost?','Ин чипта чанд пул аст?'],['I need some help with this problem.','Ба ман барои ҳалли ин масъала каме ёрӣ лозим аст.'],['My family lives in Tajikistan.','Оилаи ман дар Тоҷикистон зиндагӣ мекунад.'],['What time does the shop close?','Мағоза соати чанд баста мешавад?'],['I have an appointment tomorrow.','Ман пагоҳ вақти қабул дорам.'],['Could you send me the address?','Метавонед суроғаро ба ман фиристед?'],['The weather is beautiful today.','Имрӯз ҳаво хеле хуб аст.'],['I am looking for a new job.','Ман кори нав меҷӯям.'],['Thank you for your patience.','Барои сабратон ташаккур.'],['We are ready to start the meeting.','Мо барои оғози ҷаласа омодаем.'],['Please write your name here.','Лутфан, номи худро дар ин ҷо нависед.'],['I forgot my phone at home.','Ман телефонамро дар хона фаромӯш кардам.'],['Can I pay in cash?','Метавонам нақдӣ пардохт кунам?'],['The bus is ten minutes late.','Автобус даҳ дақиқа дер мекунад.'],['I will be there on time.','Ман сари вақт он ҷо мешавам.'],['Could you explain the rules?','Метавонед қоидаҳоро фаҳмонед?'],['This is my first visit to London.','Ин аввалин сафари ман ба Лондон аст.'],['I would like to improve my speaking.','Ман мехоҳам маҳорати гуфтугӯямро беҳтар кунам.'],['The meeting has been postponed.','Ҷаласа ба вақти дигар гузаронида шуд.'],['Please check your email.','Лутфан, почтаи электронии худро санҷед.'],['I agree with your suggestion.','Ман бо пешниҳоди шумо розӣ ҳастам.'],['We need to finish this project today.','Мо бояд ин лоиҳаро имрӯз ба анҷом расонем.'],['Where can I find a pharmacy?','Дорухонаро аз куҷо ёфта метавонам?'],['I do not have enough time.','Ман вақти кофӣ надорам.'],['Have a safe journey!','Сафари бехатар дошта бошед!']
];
const businessGroups=[
{title:'Дар ҷои кор',en:'At work',items:[['I am responsible for this task.','Ман барои ин супориш масъул ҳастам.'],['Could we discuss the schedule?','Метавонем ҷадвалро муҳокима кунем?'],['I will send an update by email.','Ман тавассути почтаи электронӣ навсозиро мефиристам.'],['Let us review the results.','Биёед натиҷаҳоро баррасӣ кунем.'],['I need clarification on this point.','Дар ин масъала ба ман шарҳи иловагӣ лозим аст.'],['I can complete it by Friday.','Ман метавонам онро то рӯзи ҷумъа анҷом диҳам.']]},
{title:'Мусоҳибаи корӣ',en:'Job interview',items:[['Could you tell me about yourself?','Метавонед дар бораи худ нақл кунед?'],['I am hardworking and responsible.','Ман меҳнатдӯст ва масъулиятшинос ҳастам.'],['I learn new skills quickly.','Ман малакаҳои навро зуд меомӯзам.'],['Why do you want this job?','Чаро ин корро мехоҳед?'],['I am interested in this position.','Ман ба ин вазифа шавқ дорам.'],['When can I start?','Ман кай метавонам ба кор сар кунам?']]},
{title:'Резюме / CV',en:'CV and resume',items:[['Work experience','Таҷрибаи корӣ'],['Education','Маълумот / таҳсил'],['Skills','Малакаҳо'],['Languages','Забонҳо'],['References available upon request.','Маълумотномаҳо бо дархост пешниҳод мешаванд.'],['I have experience in customer service.','Ман дар хизматрасонӣ ба мизоҷон таҷриба дорам.']]},
{title:'Почтаи корӣ',en:'Business email',items:[['Dear Hiring Manager,','Муҳтарам роҳбари шуъбаи кадрҳо,'],['I am writing to apply for the position.','Ман барои дархости ин вазифа мактуб менависам.'],['Please find my CV attached.','Лутфан, резюмеи маро дар замима бинед.'],['Thank you for your consideration.','Барои баррасии дархости ман ташаккур.'],['I look forward to hearing from you.','Интизори ҷавоби шумо ҳастам.'],['Kind regards,','Бо эҳтиром,']]}
];

const quizSets=[
{title:'Санҷиши 1 · Асосҳо',questions:[
{q:'Калимаи «Apple» ба забони тоҷикӣ чӣ маъно дорад?',type:'single',opts:['Себ','Нок','Ангур','Олу'],a:[0],why:['Apple маънои себро дорад.','Нок ба англисӣ pear аст.','Ангур ба англисӣ grapes аст.','Олу ба англисӣ plum аст.'],hint:'Ин мева одатан сурх ё сабз мешавад.'},
{q:'Ҷумлаи «I am a student» чӣ маъно дорад?',type:'single',opts:['Ман омӯзгор ҳастам','Ман донишҷӯ ҳастам','Ман дар хона ҳастам','Ман кор мекунам'],a:[1],why:['Ин ҷумла маънои омӯзгор буданро надорад.','I am a student маънои ман донишҷӯ ҳастамро дорад.','Ин маънои I am at home аст.','Ин маънои I work аст.'],hint:'Ба калимаи student диққат диҳед.'},
{q:'Ҷумлаи «She goes to school» дар кадом замон аст?',type:'single',opts:['Present Simple','Past Simple','Future Simple','Present Continuous'],a:[0],why:['Goes бо she дар Present Simple истифода мешавад.','Past Simple шакли went дорад.','Future Simple одатан will мегирад.','Present Continuous шакли is going дорад.'],hint:'Ин ҷумла амали одатиро нишон медиҳад.'},
{q:'Ҷойи холиро пур кунед: They ___ playing football now.',type:'single',opts:['is','am','are','be'],a:[2],why:['Is бо he, she, it истифода мешавад.','Am бо I истифода мешавад.','They are playing шакли дуруст аст.','Be дар ин ҷумла мустақилона намеояд.'],hint:'Playing now амали ҳозир идомадоштаро нишон медиҳад.'},
{q:'Кадом калимаҳо феъл мебошанд? Ҳамаи ҷавобҳои дурустро интихоб кунед.',type:'multi',opts:['Run','Beautiful','Write','Table'],a:[0,2],why:['Run феъл аст: давидан.','Beautiful сифат аст.','Write феъл аст: навиштан.','Table исм аст: миз.'],hint:'Феъл амал ё ҳаракатро ифода мекунад.'},
{q:'Дар Present Simple бо he, she ё it барои сохтани савол одатан does истифода мешавад.',type:'tf',opts:['Дуруст','Нодуруст'],a:[0],why:['Does бо he, she ва it меояд: Does she work?','Ин қоида дуруст аст; does истифода мешавад.'],hint:'Фарқи do ва does-ро ба ёд оред.'},
{q:'Калимаи «Went» шакли замони гузаштаи феъли «Go» мебошад.',type:'tf',opts:['Дуруст','Нодуруст'],a:[0],why:['Went шакли Past Simple-и go мебошад.','Went воқеан шакли гузаштаи go аст.'],hint:'Ин феъли бенизом аст.'}
]},
{title:'Санҷиши 2 · Сатҳи миёна',questions:[
{q:'Ҷойи холиро пур кунед: My brother ___ English every day.',type:'single',opts:['study','studies','studying','studied'],a:[1],why:['Бо My brother шакли studies лозим аст.','My brother шахси сеюми танҳо аст: studies.','Studying бе феъли ёрирасон нодуруст аст.','Studied замони гузаштаро нишон медиҳад.'],hint:'Ба қоидаи феъл барои he, she, it диққат диҳед.'},
{q:'Калимаи «Although» чӣ маъно дорад?',type:'single',opts:['Зеро ки','Аммо','Ҳарчанд','Бинобар ин'],a:[2],why:['Зеро ки — because.','Аммо — but.','Although маънои ҳарчандро дорад.','Бинобар ин — therefore.'],hint:'Ин калима зиддиятро нишон медиҳад.'},
{q:'Ҷумлаи «I have lived here for five years» кадом замон аст?',type:'single',opts:['Present Perfect','Past Simple','Present Continuous','Future Perfect'],a:[0],why:['Have lived шакли Present Perfect аст.','Past Simple дар ин ҷо lived бе have мешавад.','Present Continuous шакли am living дорад.','Future Perfect шакли will have lived дорад.'],hint:'Ба сохтори have + шакли сеюми феъл аҳамият диҳед.'},
{q:'«If it rains, we will stay at home» кадом навъи шартиро нишон медиҳад?',type:'single',opts:['Zero Conditional','First Conditional','Second Conditional','Third Conditional'],a:[1],why:['Zero Conditional одатан present simple-ро дар ҳар ду қисм истифода мебарад.','If + Present Simple ва will + verb сохтори First Conditional аст.','Second Conditional одатан If + Past Simple ва would дорад.','Third Conditional бо If + Past Perfect сохта мешавад.'],hint:'Шарт дар бораи эҳтимоли воқеии оянда аст.'},
{q:'Кадом ҷумлаҳо аз ҷиҳати грамматика дурустанд? Ҳамаи ҷавобҳои дурустро интихоб кунед.',type:'multi',opts:["She doesn't like coffee.","He don't play football.","We are watching TV.","They is happy."],a:[0,2],why:["Doesn't бо феъли асосии like дуруст аст.","Бо he шакли doesn't лозим аст.","Are watching шакли дурусти Present Continuous аст.","Бо they шакли are истифода мешавад."],hint:'Мувофиқати феълро бо ҷонишин санҷед.'},
{q:'Дар ҷумлаи «I have never been to London», калимаи «never» маънои «ҳеҷ гоҳ»-ро дорад.',type:'tf',opts:['Дуруст','Нодуруст'],a:[0],why:['Never маънои ҳеҷ гоҳро дорад.','Never воқеан маънои ҳеҷ гоҳро дорад.'],hint:'Ин калима рух надодани амалро ифода мекунад.'},
{q:'Дар ҷумлаи «She is more taller than her sister», шакли муқоисавӣ дуруст сохта шудааст.',type:'tf',opts:['Дуруст','Нодуруст'],a:[1],why:['More taller нодуруст аст; taller аллакай шакли муқоисавӣ мебошад.','Дуруст: «She is taller than her sister».'],hint:'More ва пасванди -er-ро якҷоя истифода накунед.'}
]}
];
let activeQuizSet=0,quizIndex=0,quizPicks=[],quizSubmitted=false,quizScore=0;
const lessonInfo={title:'Present Simple · Амалҳои ҳаррӯза',text:'Present Simple барои одатҳо, корҳои ҳаррӯза ва ҳақиқатҳо истифода мешавад.',examples:[['I work every day.','Ман ҳар рӯз кор мекунам.'],['She studies English.','Ӯ забони англисиро меомӯзад.'],['They live in Tajikistan.','Онҳо дар Тоҷикистон зиндагӣ мекунанд.']],rule:'Бо he, she, it ба феъл одатан -s ё -es илова мешавад. Барои савол does ва барои инкор doesn’t истифода мешавад.'};
function renderQuizHome(){
 content().innerHTML='<p style="color:var(--muted);line-height:1.7">Саволҳои санҷишҳои қаблӣ ба барнома илова шуданд. Ҳар санҷиш 7 савол дорад ва натиҷа нишон дода мешавад.</p><div class="learningGrid">'+quizSets.map((s,i)=>card(s.title,'7 савол · натиҷа ва шарҳ',i,'quiz-set')).join('')+'</div>';
}
function renderQuizQuestion(){
 const set=quizSets[activeQuizSet],q=set.questions[quizIndex],multi=q.type==='multi',tf=q.type==='tf';
 const options=q.opts.map((o,i)=>'<button type="button" class="learningToken" data-quiz-opt="'+i+'" '+(quizPicks.includes(i)?'style="border-color:var(--gold);background:#17533d"':'')+' '+(quizSubmitted?'disabled':'')+'>'+((multi?'☐ ':tf?'':'◯ '))+esc(o)+'</button>').join('');
 const feedback=quizSubmitted?'<div class="builderFeedback '+(q.a.length===quizPicks.length&&q.a.every(x=>quizPicks.includes(x))?'good':'try')+'">'+q.opts.map((o,i)=>'<p><b>'+esc(o)+':</b> '+esc(q.why[i])+'</p>').join('')+'</div>':'';
 content().innerHTML='<button type="button" class="learningToken" data-quiz-home>← Санҷишҳо</button><div class="builderTop"><span class="builderStat">'+esc(set.title)+'</span><span class="builderStat">'+(quizIndex+1)+' аз '+set.questions.length+'</span></div><div class="builderProgress"><span style="width:'+Math.round((quizIndex+1)/set.questions.length*100)+'%"></span></div><div class="panel"><p class="builderPrompt">'+esc(q.q)+'</p><div class="learningLine">'+options+'</div>'+(multi?'<p class="meta">Якчанд ҷавобро интихоб кардан мумкин аст.</p>':'')+'<button type="button" class="builderPrimary" data-quiz-check '+(quizSubmitted?'disabled':'')+'>Санҷидани ҷавоб</button>'+feedback+'</div><div class="builderActions" style="margin-top:12px">'+(quizSubmitted?'<button type="button" class="builderPrimary" data-quiz-next>'+(quizIndex===set.questions.length-1?'Натиҷа':'Саволи навбатӣ →')+'</button>':'')+'<button type="button" class="learningToken" data-quiz-hint>Кӯмак</button></div><div id="quizHint" class="meta"></div>';
}
function finishQuiz(){
 const set=quizSets[activeQuizSet],pct=Math.round(quizScore/set.questions.length*100);
 localStorage.setItem('shohin_lughat_last_quiz',JSON.stringify({title:set.title,score:quizScore,total:set.questions.length,percent:pct,date:new Date().toISOString()}));
 content().innerHTML='<div class="panel"><div class="eyebrow">НАТИҶАИ САНҶИШ</div><h2 style="color:var(--gold)">'+quizScore+' аз '+set.questions.length+' ҷавоби дуруст</h2><p class="builderPrompt">'+pct+'% · '+(pct>=85?'Аъло!':pct>=60?'Натиҷаи хуб.':'Боз такрор кунед.')+'</p><div class="builderProgress"><span style="width:'+pct+'%"></span></div><div class="learningActions"><button type="button" class="builderPrimary" data-quiz-retry>Аз нав гузаштан</button><button type="button" class="learningToken" data-quiz-home>Ба санҷишҳо</button></div></div>';
}
function openQuizSet(i){activeQuizSet=i;quizIndex=0;quizPicks=[];quizSubmitted=false;quizScore=0;renderQuizQuestion()}
function openLastLesson(){
 const saved=localStorage.getItem('shohin_lughat_last_lesson')||'present-simple';
 content().innerHTML='<div class="section"><strong>Дарси охирин</strong><span>Идома додани омӯзиш</span></div><article class="panel"><div class="eyebrow">ДАРСИ ОХИРИН · ENGLISH</div><h2 style="color:var(--gold)">'+esc(lessonInfo.title)+'</h2><p style="line-height:1.8">'+esc(lessonInfo.text)+'</p><div class="learningPhrase"><b>Қоидаи асосӣ</b><p>'+esc(lessonInfo.rule)+'</p></div>'+lessonInfo.examples.map(x=>'<div class="learningPhrase"><strong>'+esc(x[0])+'</strong><p>'+esc(x[1])+'</p><button type="button" class="learningToken" data-lesson-speak="'+esc(x[0])+'">🔊 Шунидан</button></div>').join('')+'<div class="builderActions" style="margin-top:14px"><button type="button" class="builderPrimary" data-lesson-continue>Идома додани машқ →</button></div><p class="meta">Дарси охирин: '+(saved==='present-simple'?'Present Simple':'Present Simple')+'</p></article>';
}

let page='',phraseIndex=-1,businessIndex=-1,exerciseIndex=0,tokenList=[],selected=[],translationDirection=0,builderScore=0,builderCompleted=[];
const content=()=> $('learningToolsContent');
function speak(s){if(!('speechSynthesis'in window)){alert('Ин дастгоҳ имконияти шунидани овозро дастгирӣ намекунад.');return}speechSynthesis.cancel();const u=new SpeechSynthesisUtterance(s);u.lang='en-US';speechSynthesis.speak(u)}
function card(title,sub,index,attr){return '<button class="learningCard" type="button" data-'+attr+'="'+index+'"><strong>'+esc(title)+'</strong><span>'+esc(sub)+'</span></button>'}
function openPage(name){
 if(typeof showPage==='function')showPage('home');
 $('homeStories').hidden=true;
 ['conversationSection','storiesSection','myAppsSection'].forEach(id=>$(id).hidden=true);
 $('words').hidden=true;$('dictionaryHeading').hidden=true;$('topSearch').hidden=true;
 $('learningToolsSection').hidden=false;
 page=name;
 const titles={phrasebook:'Ибораҳои омода',builder:'Сохтани ҷумлаҳо',translate:'Тарҷумаи ҷумлаҳо',numbers:'Рақамҳо ва санаҳо',business:'Забони англисии корӣ',quicktest:'Санҷиши зуд',lastlesson:'Дарси охирин'};
 $('learningToolsTitle').textContent=titles[name];
 if(name==='phrasebook'){phraseIndex=-1;renderPhrases()}
 if(name==='builder'){exerciseIndex=0;builderScore=0;builderCompleted=[];renderBuilder()}
 if(name==='translate'){translationDirection=0;renderTranslations()}
 if(name==='numbers')renderNumbers()
 if(name==='business'){businessIndex=-1;renderBusiness()}
 if(name==='quicktest')renderQuizHome();\n if(name==='lastlesson'){localStorage.setItem('shohin_lughat_last_lesson','present-simple');openLastLesson()}
}
function renderPhrases(){
 if(phraseIndex<0){content().innerHTML='<p style="color:var(--muted);line-height:1.7">Вазъиятро интихоб кунед. Дар ҳар бахш ибораҳои омода бо тарҷумаи тоҷикӣ мавҷуданд.</p><div class="learningGrid">'+phraseGroups.map((g,i)=>card(g.title,g.en+' · '+g.items.length+' ибора',i,'phrase-category')).join('')+'</div>';return}
 const g=phraseGroups[phraseIndex];
 content().innerHTML='<button class="learningToken" data-phrase-back type="button">← Ҳамаи вазъиятҳо</button><div class="section"><strong>'+esc(g.title)+'</strong><span>'+esc(g.en)+'</span></div>'+g.items.map((p,i)=>'<div class="learningPhrase"><strong>'+esc(p[0])+'</strong><p>'+esc(p[1])+'</p><button class="learningToken" data-phrase-speak="'+i+'" type="button">🔊 Шунидан</button></div>').join('');
}
function shuffle(a){a=a.slice();for(let i=a.length-1;i>0;i--){const j=Math.floor(Math.random()*(i+1));[a[i],a[j]]=[a[j],a[i]]}return a}
function norm(s){return String(s).toLowerCase().replace(/[.!?,;:’']/g,'').replace(/\s+/g,' ').trim()}
function renderBuilder(){
 const p=exercises[exerciseIndex];tokenList=shuffle(p[0].split(' '));selected=[];
 const progress=Math.round(builderCompleted.length/exercises.length*100);
 content().innerHTML='<p style="color:var(--muted);line-height:1.7">Калимаҳоро бо тартиби дуруст интихоб кунед ва аз онҳо ҷумлаи англисӣ созед. Барои хориҷ кардани калима ба худи он дар қисми ҷавоб пахш кунед.</p><div class="builderTop"><span class="builderStat">Машқи '+(exerciseIndex+1)+' аз '+exercises.length+'</span><span class="builderStat">Ҷавобҳои дуруст: <b id="builderScore">'+builderScore+'</b></span></div><div class="builderProgress"><span id="builderProgressFill" style="width:'+progress+'%"></span></div><div class="panel" style="margin:12px 0"><strong style="color:var(--gold)">Тарҷума ба тоҷикӣ</strong><p class="builderPrompt">'+esc(p[1])+'</p></div><div class="builderTop"><strong>Ҷумлаи англисии шумо</strong><span class="builderStat" id="builderWordCount">0 аз '+tokenList.length+' калима</span></div><div class="learningAnswer" id="builderAnswer"><span class="builderStat">Калимаҳоро аз поён интихоб кунед…</span></div><div class="learningLine" id="builderTokens">'+tokenList.map((w,i)=>'<button class="learningToken" type="button" data-token="'+i+'">'+esc(w)+'</button>').join('')+'</div><div class="builderActions"><button class="builderPrimary" type="button" data-check>Санҷидани ҷавоб</button><button class="learningToken" type="button" data-hint>Кӯмак</button><button class="learningToken" type="button" data-reset>Аз нав</button><button class="learningToken" type="button" data-next>Ҷумлаи навбатӣ →</button></div><div id="builderResult" aria-live="polite"></div>';
}
function updateBuilder(){
 $('builderAnswer').innerHTML=selected.length?selected.map(i=>'<button class="learningToken" type="button" data-remove="'+i+'" title="Барои хориҷ кардан пахш кунед">'+esc(tokenList[i])+' ×</button>').join(''):'<span class="builderStat">Калимаҳоро аз поён интихоб кунед…</span>';
 $('builderWordCount').textContent=selected.length+' аз '+tokenList.length+' калима';
 const used=new Set(selected);
 $('builderTokens').innerHTML=tokenList.map((w,i)=>'<button class="learningToken" type="button" data-token="'+i+'" '+(used.has(i)?'disabled':'')+'>'+esc(w)+'</button>').join('');
}
function renderTranslations(){
 content().innerHTML='<p style="color:var(--muted);line-height:1.7">Маҷмӯаи офлайнии ҷумлаҳо. Ҷустуҷӯро бо матни англисӣ ё тоҷикӣ анҷом диҳед. Ин восита тарҷумаи худкори ҳар гуна матнро иҷро намекунад.</p><input class="learningInput" id="translationSearch" placeholder="Ҷустуҷӯи ҷумла..." autocomplete="off"><div class="learningLine"><button class="learningToken" data-direction="0" type="button">English → Тоҷикӣ</button><button class="learningToken" data-direction="1" type="button">Тоҷикӣ → English</button></div><div id="translationResults"></div>';
 updateTranslations();
}
function updateTranslations(){
 const q=($('translationSearch')?.value||'').trim().toLowerCase();
 const list=translations.filter(p=>!q||p[0].toLowerCase().includes(q)||p[1].toLowerCase().includes(q));
 $('translationResults').innerHTML=list.length?list.map(p=>'<div class="learningPhrase"><strong>'+esc(p[translationDirection])+'</strong><p>'+esc(p[1-translationDirection])+'</p><button class="learningToken" type="button" data-speak="'+esc(p[0])+'">🔊 Шунидан</button></div>').join(''):'<div class="state">Ҷумла ёфт нашуд. Калимаи дигарро ҷустуҷӯ кунед.</div>';
}
const ones=['zero','one','two','three','four','five','six','seven','eight','nine','ten','eleven','twelve','thirteen','fourteen','fifteen','sixteen','seventeen','eighteen','nineteen'];
const tens=['','','twenty','thirty','forty','fifty','sixty','seventy','eighty','ninety'];
function words(n){if(n<20)return ones[n];if(n<100)return tens[Math.floor(n/10)]+(n%10?'-'+ones[n%10]:'');if(n<1000)return ones[Math.floor(n/100)]+' hundred'+(n%100?' '+words(n%100):'');if(n<1000000)return words(Math.floor(n/1000))+' thousand'+(n%1000?' '+words(n%1000):'');return ''}
function renderNumbers(){
 const days=['Monday — Душанбе','Tuesday — Сешанбе','Wednesday — Чоршанбе','Thursday — Панҷшанбе','Friday — Ҷумъа','Saturday — Шанбе','Sunday — Якшанбе'];
 const months=['January — Январ','February — Феврал','March — Март','April — Апрел','May — Май','June — Июн','July — Июл','August — Август','September — Сентябр','October — Октябр','November — Ноябр','December — Декабр'];
 content().innerHTML='<p style="color:var(--muted);line-height:1.7">Рақамеро аз 0 то 999999 ворид кунед ё вақтро интихоб намоед. Рӯзҳои ҳафта ва моҳҳои сол низ оварда шудаанд.</p><div class="panel"><label for="numberInput">Рақам / Number</label><input class="learningInput" id="numberInput" type="number" min="0" max="999999" placeholder="Масалан: 125"><div class="learningResult" id="numberResult">Натиҷа дар ин ҷо нишон дода мешавад.</div></div><div class="panel" style="margin-top:12px"><label for="timeInput">Вақт / Time</label><input class="learningInput" id="timeInput" type="time" value="09:30"><div class="learningResult" id="timeResult"></div></div><div class="panel" style="margin-top:12px"><label for="dateInput">Сана / Date</label><input class="learningInput" id="dateInput" type="date"><div class="learningResult" id="dateResult">Санаро интихоб кунед, то шакли англисии онро бинед.</div></div><div class="section"><strong>Рӯзҳои ҳафта / Days</strong></div><div class="learningGrid">'+days.map(x=>'<div class="learningCard"><span>'+esc(x)+'</span></div>').join('')+'</div><div class="section"><strong>Моҳҳои сол / Months</strong></div><div class="learningGrid">'+months.map(x=>'<div class="learningCard"><span>'+esc(x)+'</span></div>').join('')+'</div>';
 updateNumber();updateTime();
}
function ordinal(n){const o=['','first','second','third','fourth','fifth','sixth','seventh','eighth','ninth','tenth','eleventh','twelfth','thirteenth','fourteenth','fifteenth','sixteenth','seventeenth','eighteenth','nineteenth'];if(n<20)return o[n];if(n===20)return 'twentieth';if(n===30)return 'thirtieth';return words(Math.floor(n/10)*10)+'-'+ordinal(n%10)}
function updateDate(){const el=$('dateInput');if(!el||!el.value)return;const d=new Date(el.value+'T12:00:00');if(Number.isNaN(d.getTime()))return;const months=['January','February','March','April','May','June','July','August','September','October','November','December'];const day=d.getDate(),year=d.getFullYear();$('dateResult').textContent='Ба забони англисӣ: '+ordinal(day)+' of '+months[d.getMonth()]+' '+words(year)+' · Ба тоҷикӣ: '+day+' '+months[d.getMonth()]+' '+year;}
function updateNumber(){const el=$('numberInput');if(!el)return;const raw=el.value,n=Number(raw);$('numberResult').textContent=raw===''?'Натиҷа дар ин ҷо нишон дода мешавад.':Number.isInteger(n)&&n>=0&&n<=999999?words(n):'Рақам бояд аз 0 то 999999 бошад.'}
function updateTime(){const el=$('timeInput');if(!el)return;const a=el.value.split(':').map(Number);if(a.length!==2||a.some(Number.isNaN))return;const h=a[0],m=a[1],period=h<12?'a.m.':'p.m.',hour=h%12||12;let phrase=words(hour);if(m===15)phrase='quarter past '+words(hour);else if(m===30)phrase='half past '+words(hour);else if(m===45)phrase='quarter to '+words((hour%12)+1);else if(m>0&&m<30)phrase=words(m)+' past '+words(hour);else if(m>30)phrase=words(60-m)+' to '+words((hour%12)+1);else phrase+=" o'clock";$('timeResult').textContent=phrase+' '+period}
function renderBusiness(){
 if(businessIndex<0){content().innerHTML='<p style="color:var(--muted);line-height:1.7">Ибораҳо барои муошират дар ҷойи кор, мусоҳибаи корӣ, тарҷумаи ҳол ва мукотибаи корӣ.</p><div class="learningGrid">'+businessGroups.map((g,i)=>card(g.title,g.en+' · '+g.items.length+' намуна',i,'business-category')).join('')+'</div>';return}
 const g=businessGroups[businessIndex];content().innerHTML='<button class="learningToken" type="button" data-business-back>← Ҳамаи бахшҳо</button><div class="section"><strong>'+esc(g.title)+'</strong><span>'+esc(g.en)+'</span></div>'+g.items.map((p,i)=>'<div class="learningPhrase"><strong>'+esc(p[0])+'</strong><p>'+esc(p[1])+'</p><button class="learningToken" type="button" data-business-speak="'+i+'">🔊 Шунидан</button></div>').join('');
}
$('phrasebookOpen').addEventListener('click',()=>openPage('phrasebook'));
$('builderOpen').addEventListener('click',()=>openPage('builder'));
$('translateOpen').addEventListener('click',()=>openPage('translate'));
$('numbersOpen').addEventListener('click',()=>openPage('numbers'));
$('businessOpen').addEventListener('click',()=>openPage('business'));
$('quickTestOpen').addEventListener('click',()=>openPage('quicktest'));\n$('lastLessonOpen').addEventListener('click',()=>openPage('lastlesson'));
$('learningToolsBack').addEventListener('click',()=>{if(typeof showPage==='function')showPage('home');$('learningToolsSection').hidden=true;$('homeStories').hidden=false;page=''});
content().addEventListener('click',e=>{
 let qb=e.target.closest('[data-quiz-set]');if(qb){openQuizSet(+qb.dataset.quizSet);return}
 if(e.target.closest('[data-quiz-home]')){renderQuizHome();return}
 qb=e.target.closest('[data-quiz-opt]');if(qb&&!quizSubmitted){const n=+qb.dataset.quizOpt,q=quizSets[activeQuizSet].questions[quizIndex];if(q.type==='multi'){quizPicks=quizPicks.includes(n)?quizPicks.filter(x=>x!==n):[...quizPicks,n]}else{quizPicks=[n]}renderQuizQuestion();return}
 if(e.target.closest('[data-quiz-check]')){if(!quizPicks.length){$('quizHint').textContent='Аввал ҷавобро интихоб кунед.';return}quizSubmitted=true;const q=quizSets[activeQuizSet].questions[quizIndex];if(q.a.length===quizPicks.length&&q.a.every(x=>quizPicks.includes(x)))quizScore++;renderQuizQuestion();return}
 if(e.target.closest('[data-quiz-next]')){if(quizIndex===quizSets[activeQuizSet].questions.length-1){finishQuiz()}else{quizIndex++;quizPicks=[];quizSubmitted=false;renderQuizQuestion()}return}
 if(e.target.closest('[data-quiz-retry]')){openQuizSet(activeQuizSet);return}
 if(e.target.closest('[data-quiz-hint]')){const q=quizSets[activeQuizSet].questions[quizIndex];$('quizHint').textContent=q.hint;return}
 if(e.target.closest('[data-lesson-continue]')){localStorage.setItem('shohin_lughat_last_lesson','present-simple');openPage('builder');return}
 qb=e.target.closest('[data-lesson-speak]');if(qb){speak(qb.dataset.lessonSpeak);return}
 
 let b=e.target.closest('[data-phrase-category]');if(b){phraseIndex=+b.dataset.phraseCategory;renderPhrases();return}
 if(e.target.closest('[data-phrase-back]')){phraseIndex=-1;renderPhrases();return}
 b=e.target.closest('[data-phrase-speak]');if(b){speak(phraseGroups[phraseIndex].items[+b.dataset.phraseSpeak][0]);return}
 b=e.target.closest('[data-business-category]');if(b){businessIndex=+b.dataset.businessCategory;renderBusiness();return}
 if(e.target.closest('[data-business-back]')){businessIndex=-1;renderBusiness();return}
 b=e.target.closest('[data-business-speak]');if(b){speak(businessGroups[businessIndex].items[+b.dataset.businessSpeak][0]);return}
 b=e.target.closest('[data-token]');if(b){const i=+b.dataset.token;if(!selected.includes(i)){selected.push(i);updateBuilder()}return}
 b=e.target.closest('[data-remove]');if(b){selected=selected.filter(i=>i!==+b.dataset.remove);updateBuilder();return}
 if(e.target.closest('[data-check]')){
 const answer=selected.map(i=>tokenList[i]).join(' '),correct=exercises[exerciseIndex][0],ok=norm(answer)===norm(correct);
 if(ok&&!builderCompleted.includes(exerciseIndex)){builderCompleted.push(exerciseIndex);builderScore++}
 $('builderScore').textContent=builderScore;
 $('builderProgressFill').style.width=Math.round(builderCompleted.length/exercises.length*100)+'%';
 $('builderResult').innerHTML='<div class="builderFeedback '+(ok?'good':'try')+'">'+(ok?'Офарин! Ҷумла дуруст сохта шуд.':'Ҳоло ҷавоб дуруст нест. Калимаҳоро аз нав тартиб диҳед ва кӯшиш кунед.')+(ok?'':'<br><b>Ҷавоби дуруст:</b> '+esc(correct))+'</div>';
 return
 }
 if(e.target.closest('[data-hint]')){const correct=exercises[exerciseIndex][0].split(' '),used=new Set(selected);const next=correct.find(w=>{const idx=tokenList.findIndex((t,i)=>t===w&&!used.has(i));return idx>=0});if(next){const idx=tokenList.findIndex((t,i)=>t===next&&!used.has(i));selected.push(idx);updateBuilder()}return}
 if(e.target.closest('[data-reset]')){selected=[];updateBuilder();$('builderResult').innerHTML='';return}
 if(e.target.closest('[data-next]')){exerciseIndex=(exerciseIndex+1)%exercises.length;renderBuilder();return}
 b=e.target.closest('[data-direction]');if(b){translationDirection=+b.dataset.direction;updateTranslations();return}
 b=e.target.closest('[data-speak]');if(b){speak(b.dataset.speak);return}
});
content().addEventListener('input',e=>{if(e.target.id==='translationSearch')updateTranslations();if(e.target.id==='numberInput')updateNumber();if(e.target.id==='timeInput')updateTime();if(e.target.id==='dateInput')updateDate()});
})();