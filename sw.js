const CACHE_NAME='shohin-lugat-v5';
const CORE=['./','./index.html','./dictionary/words.json','./assets/stories/nightingale.svg','./assets/stories/devoted-friend.svg'];
const REMOTE_IMAGES=["https://images.unsplash.com/photo-1511818966892-d7d671e672a2?auto=format&fit=crop&w=900&q=80","https://images.unsplash.com/photo-1512389142860-9c449e58a543?auto=format&fit=crop&w=900&q=80","https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=900&q=80","https://images.unsplash.com/photo-1511497584788-876760111969?auto=format&fit=crop&w=900&q=80","https://images.unsplash.com/photo-1500375592092-40eb2168fd21?auto=format&fit=crop&w=900&q=80","https://images.unsplash.com/photo-1470252649378-9c29740c9fa8?auto=format&fit=crop&w=900&q=80","https://images.unsplash.com/photo-1416879595882-3373a0480b5b?auto=format&fit=crop&w=900&q=80","https://images.unsplash.com/photo-1553284965-83fd3e82fa5a?auto=format&fit=crop&w=900&q=80","https://images.unsplash.com/photo-1507842217343-583bb7270b66?auto=format&fit=crop&w=900&q=80"];
self.addEventListener('install',event=>{
 event.waitUntil((async()=>{
  const cache=await caches.open(CACHE_NAME);
  await cache.addAll(CORE);
  await Promise.all(REMOTE_IMAGES.map(async url=>{
   try{
    const response=await fetch(url,{mode:'no-cors',signal:AbortSignal.timeout(10000)});
    if(response)await cache.put(url,response);
   }catch(error){}
  }));
  await self.skipWaiting();
 })());
});
self.addEventListener('activate',event=>{
 event.waitUntil((async()=>{
  const keys=await caches.keys();
  await Promise.all(keys.filter(key=>key!==CACHE_NAME).map(key=>caches.delete(key)));
  await self.clients.claim();
 })());
});
self.addEventListener('fetch',event=>{
 const request=event.request;
 if(request.method!=='GET')return;
 const remoteImage=REMOTE_IMAGES.includes(request.url);
 if(new URL(request.url).origin!==self.location.origin&&!remoteImage)return;
 event.respondWith((async()=>{
  const cached=await caches.match(request);
  if(cached)return cached;
  try{
   const response=await fetch(request);
   if(response&&(response.ok||response.type==='opaque')){
    const cache=await caches.open(CACHE_NAME);
    await cache.put(request,response.clone());
   }
   return response;
  }catch(error){return cached;}
 })());
});