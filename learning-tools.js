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
let page='',phraseIndex=-1,businessIndex=-1,exerciseIndex=0,tokenList=[],selected=[],translationDirection=0;
const content=()=> $('learningToolsContent');
function speak(s){if(!('speechSynthesis'in window)){alert('Овоздиҳӣ дар ин дастгоҳ дастгирӣ намешавад.');return}speechSynthesis.cancel();const u=new SpeechSynthesisUtterance(s);u.lang='en-US';speechSynthesis.speak(u)}
function card(title,sub,index,attr){return '<button class="learningCard" type="button" data-'+attr+'="'+index+'"><strong>'+esc(title)+'</strong><span>'+esc(sub)+'</span></button>'}
function openPage(name){
 if(typeof showPage==='function')showPage('home');
 $('homeStories').hidden=true;
 ['conversationSection','storiesSection','myAppsSection'].forEach(id=>$(id).hidden=true);
 $('words').hidden=true;$('dictionaryHeading').hidden=true;$('topSearch').hidden=true;
 $('learningToolsSection').hidden=false;
 page=name;
 const titles={phrasebook:'Фразовый справочник',builder:'Построение предложений',translate:'Перевод предложений',numbers:'Числа и даты',business:'Деловой английский'};
 $('learningToolsTitle').textContent=titles[name];
 if(name==='phrasebook'){phraseIndex=-1;renderPhrases()}
 if(name==='builder'){exerciseIndex=0;renderBuilder()}
 if(name==='translate'){translationDirection=0;renderTranslations()}
 if(name==='numbers')renderNumbers()
 if(name==='business'){businessIndex=-1;renderBusiness()}
}
function renderPhrases(){
 if(phraseIndex<0){content().innerHTML='<p style="color:var(--muted);line-height:1.7">Выберите ситуацию. В каждом разделе есть готовые выражения с таджикским переводом.</p><div class="learningGrid">'+phraseGroups.map((g,i)=>card(g.title,g.en+' · '+g.items.length+' выражений',i,'phrase-category')).join('')+'</div>';return}
 const g=phraseGroups[phraseIndex];
 content().innerHTML='<button class="learningToken" data-phrase-back type="button">← Все ситуации</button><div class="section"><strong>'+esc(g.title)+'</strong><span>'+esc(g.en)+'</span></div>'+g.items.map((p,i)=>'<div class="learningPhrase"><strong>'+esc(p[0])+'</strong><p>'+esc(p[1])+'</p><button class="learningToken" data-phrase-speak="'+i+'" type="button">🔊 Слушать</button></div>').join('');
}
function shuffle(a){a=a.slice();for(let i=a.length-1;i>0;i--){const j=Math.floor(Math.random()*(i+1));[a[i],a[j]]=[a[j],a[i]]}return a}
function norm(s){return String(s).toLowerCase().replace(/[.!?,;:’']/g,'').replace(/\s+/g,' ').trim()}
function renderBuilder(){
 const p=exercises[exerciseIndex];tokenList=shuffle(p[0].split(' '));selected=[];
 content().innerHTML='<p style="color:var(--muted);line-height:1.7">Выберите слова в правильном порядке, чтобы составить английское предложение.</p><div class="meta">Упражнение '+(exerciseIndex+1)+' из '+exercises.length+'</div><div class="panel" style="margin:12px 0"><strong style="color:var(--gold)">Тоҷикӣ</strong><p>'+esc(p[1])+'</p></div><strong>Ваше предложение</strong><div class="learningAnswer" id="builderAnswer"></div><div class="learningLine" id="builderTokens">'+tokenList.map((w,i)=>'<button class="learningToken" type="button" data-token="'+i+'">'+esc(w)+'</button>').join('')+'</div><div class="learningLine"><button class="promoAction" type="button" data-check>Проверить</button><button class="learningToken" type="button" data-reset>Сбросить</button><button class="learningToken" type="button" data-next>Следующее</button></div><div id="builderResult" aria-live="polite"></div>';
}
function updateBuilder(){
 $('builderAnswer').innerHTML=selected.map(i=>'<button class="learningToken" type="button" data-remove="'+i+'">'+esc(tokenList[i])+' ×</button>').join('');
 const used=new Set(selected);
 $('builderTokens').innerHTML=tokenList.map((w,i)=>'<button class="learningToken" type="button" data-token="'+i+'" '+(used.has(i)?'disabled':'')+'>'+esc(w)+'</button>').join('');
}
function renderTranslations(){
 content().innerHTML='<p style="color:var(--muted);line-height:1.7">Офлайн-подборка предложений. Ищите по английскому или таджикскому тексту; это не автоматический перевод любого произвольного текста.</p><input class="learningInput" id="translationSearch" placeholder="Поиск предложения / Ҷустуҷӯ..." autocomplete="off"><div class="learningLine"><button class="learningToken" data-direction="0" type="button">English → Тоҷикӣ</button><button class="learningToken" data-direction="1" type="button">Тоҷикӣ → English</button></div><div id="translationResults"></div>';
 updateTranslations();
}
function updateTranslations(){
 const q=($('translationSearch')?.value||'').trim().toLowerCase();
 const list=translations.filter(p=>!q||p[0].toLowerCase().includes(q)||p[1].toLowerCase().includes(q));
 $('translationResults').innerHTML=list.length?list.map(p=>'<div class="learningPhrase"><strong>'+esc(p[translationDirection])+'</strong><p>'+esc(p[1-translationDirection])+'</p><button class="learningToken" type="button" data-speak="'+esc(p[0])+'">🔊 Listen</button></div>').join(''):'<div class="state">Предложение не найдено. Попробуйте другой запрос.</div>';
}
const ones=['zero','one','two','three','four','five','six','seven','eight','nine','ten','eleven','twelve','thirteen','fourteen','fifteen','sixteen','seventeen','eighteen','nineteen'];
const tens=['','','twenty','thirty','forty','fifty','sixty','seventy','eighty','ninety'];
function words(n){if(n<20)return ones[n];if(n<100)return tens[Math.floor(n/10)]+(n%10?'-'+ones[n%10]:'');if(n<1000)return ones[Math.floor(n/100)]+' hundred'+(n%100?' '+words(n%100):'');if(n<1000000)return words(Math.floor(n/1000))+' thousand'+(n%1000?' '+words(n%1000):'');return ''}
function renderNumbers(){
 const days=['Monday — Душанбе','Tuesday — Сешанбе','Wednesday — Чоршанбе','Thursday — Панҷшанбе','Friday — Ҷумъа','Saturday — Шанбе','Sunday — Якшанбе'];
 const months=['January — Январ','February — Феврал','March — Март','April — Апрел','May — Май','June — Июн','July — Июл','August — Август','September — Сентябр','October — Октябр','November — Ноябр','December — Декабр'];
 content().innerHTML='<p style="color:var(--muted);line-height:1.7">Введите число от 0 до 999999 или выберите время. Здесь также приведены дни недели и месяцы.</p><div class="panel"><label for="numberInput">Число / Number</label><input class="learningInput" id="numberInput" type="number" min="0" max="999999" placeholder="Например: 125"><div class="learningResult" id="numberResult">Результат появится здесь.</div></div><div class="panel" style="margin-top:12px"><label for="timeInput">Время / Time</label><input class="learningInput" id="timeInput" type="time" value="09:30"><div class="learningResult" id="timeResult"></div></div><div class="panel" style="margin-top:12px"><label for="dateInput">Дата / Date</label><input class="learningInput" id="dateInput" type="date"><div class="learningResult" id="dateResult">Выберите дату, чтобы увидеть её на английском.</div></div><div class="section"><strong>Дни недели / Days</strong></div><div class="learningGrid">'+days.map(x=>'<div class="learningCard"><span>'+esc(x)+'</span></div>').join('')+'</div><div class="section"><strong>Месяцы / Months</strong></div><div class="learningGrid">'+months.map(x=>'<div class="learningCard"><span>'+esc(x)+'</span></div>').join('')+'</div>';
 updateNumber();updateTime();
}
function ordinal(n){const o=['','first','second','third','fourth','fifth','sixth','seventh','eighth','ninth','tenth','eleventh','twelfth','thirteenth','fourteenth','fifteenth','sixteenth','seventeenth','eighteenth','nineteenth'];if(n<20)return o[n];if(n===20)return 'twentieth';if(n===30)return 'thirtieth';return words(Math.floor(n/10)*10)+'-'+ordinal(n%10)}
function updateDate(){const el=$('dateInput');if(!el||!el.value)return;const d=new Date(el.value+'T12:00:00');if(Number.isNaN(d.getTime()))return;const months=['January','February','March','April','May','June','July','August','September','October','November','December'];const day=d.getDate(),year=d.getFullYear();$('dateResult').textContent='English: the '+ordinal(day)+' of '+months[d.getMonth()]+' '+words(year)+' · Тоҷикӣ: '+day+' '+months[d.getMonth()]+' '+year;}
function updateNumber(){const el=$('numberInput');if(!el)return;const raw=el.value,n=Number(raw);$('numberResult').textContent=raw===''?'Результат появится здесь.':Number.isInteger(n)&&n>=0&&n<=999999?words(n):'Число должно быть от 0 до 999999.'}
function updateTime(){const el=$('timeInput');if(!el)return;const a=el.value.split(':').map(Number);if(a.length!==2||a.some(Number.isNaN))return;const h=a[0],m=a[1],period=h<12?'a.m.':'p.m.',hour=h%12||12;let phrase=words(hour);if(m===15)phrase='quarter past '+words(hour);else if(m===30)phrase='half past '+words(hour);else if(m===45)phrase='quarter to '+words((hour%12)+1);else if(m>0&&m<30)phrase=words(m)+' past '+words(hour);else if(m>30)phrase=words(60-m)+' to '+words((hour%12)+1);else phrase+=" o'clock";$('timeResult').textContent=phrase+' '+period}
function renderBusiness(){
 if(businessIndex<0){content().innerHTML='<p style="color:var(--muted);line-height:1.7">Фразы для общения на работе, собеседований, резюме и деловой переписки.</p><div class="learningGrid">'+businessGroups.map((g,i)=>card(g.title,g.en+' · '+g.items.length+' примеров',i,'business-category')).join('')+'</div>';return}
 const g=businessGroups[businessIndex];content().innerHTML='<button class="learningToken" type="button" data-business-back>← Все разделы</button><div class="section"><strong>'+esc(g.title)+'</strong><span>'+esc(g.en)+'</span></div>'+g.items.map((p,i)=>'<div class="learningPhrase"><strong>'+esc(p[0])+'</strong><p>'+esc(p[1])+'</p><button class="learningToken" type="button" data-business-speak="'+i+'">🔊 Слушать</button></div>').join('');
}
$('phrasebookOpen').addEventListener('click',()=>openPage('phrasebook'));
$('builderOpen').addEventListener('click',()=>openPage('builder'));
$('translateOpen').addEventListener('click',()=>openPage('translate'));
$('numbersOpen').addEventListener('click',()=>openPage('numbers'));
$('businessOpen').addEventListener('click',()=>openPage('business'));
$('learningToolsBack').addEventListener('click',()=>{if(typeof showPage==='function')showPage('home');$('learningToolsSection').hidden=true;$('homeStories').hidden=false;page=''});
content().addEventListener('click',e=>{
 let b=e.target.closest('[data-phrase-category]');if(b){phraseIndex=+b.dataset.phraseCategory;renderPhrases();return}
 if(e.target.closest('[data-phrase-back]')){phraseIndex=-1;renderPhrases();return}
 b=e.target.closest('[data-phrase-speak]');if(b){speak(phraseGroups[phraseIndex].items[+b.dataset.phraseSpeak][0]);return}
 b=e.target.closest('[data-business-category]');if(b){businessIndex=+b.dataset.businessCategory;renderBusiness();return}
 if(e.target.closest('[data-business-back]')){businessIndex=-1;renderBusiness();return}
 b=e.target.closest('[data-business-speak]');if(b){speak(businessGroups[businessIndex].items[+b.dataset.businessSpeak][0]);return}
 b=e.target.closest('[data-token]');if(b){const i=+b.dataset.token;if(!selected.includes(i)){selected.push(i);updateBuilder()}return}
 b=e.target.closest('[data-remove]');if(b){selected=selected.filter(i=>i!==+b.dataset.remove);updateBuilder();return}
 if(e.target.closest('[data-check]')){const answer=selected.map(i=>tokenList[i]).join(' '),correct=exercises[exerciseIndex][0];$('builderResult').innerHTML='<div class="learningResult">'+(norm(answer)===norm(correct)?'Верно! Предложение составлено правильно.':'Пока неверно. Попробуйте ещё раз.')+'<br><b>Правильный ответ:</b> '+esc(correct)+'</div>';return}
 if(e.target.closest('[data-reset]')){selected=[];updateBuilder();$('builderResult').innerHTML='';return}
 if(e.target.closest('[data-next]')){exerciseIndex=(exerciseIndex+1)%exercises.length;renderBuilder();return}
 b=e.target.closest('[data-direction]');if(b){translationDirection=+b.dataset.direction;updateTranslations();return}
 b=e.target.closest('[data-speak]');if(b){speak(b.dataset.speak);return}
});
content().addEventListener('input',e=>{if(e.target.id==='translationSearch')updateTranslations();if(e.target.id==='numberInput')updateNumber();if(e.target.id==='timeInput')updateTime();if(e.target.id==='dateInput')updateDate()});
})();