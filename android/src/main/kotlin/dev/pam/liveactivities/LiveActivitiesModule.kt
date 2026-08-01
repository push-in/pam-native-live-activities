package dev.pam.liveactivities
import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import dev.pam.nativeapp.modules.*
import dev.pam.nativeapp.protocol.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
class LiveActivitiesModule(private val context:Context):NativeModule{private val manager=context.getSystemService(NotificationManager::class.java);private val prefs=context.getSharedPreferences("pam.live.activities",Context.MODE_PRIVATE);init{manager.createNotificationChannel(NotificationChannel("pam_live_activities","Live activities",NotificationManager.IMPORTANCE_LOW))}
 override fun invoke(method:String,payload:ByteArray,completion:ModuleCompletion){runCatching{val v=WireMap.decode(payload);when(method){"start"->{val id=UUID.randomUUID().toString();save(id,v);post(id,v);completion.ok(id)};"update"->{val id=v.text("identifier");save(id,v);post(id,v);completion.ok(id)};"end"->{val id=v.text("identifier");manager.cancel(id.hashCode());prefs.edit().remove(id).apply();completion.ok(id)};"active"->completion.success(mapOf("json" to WireValue.Text(active().toString())));else->error("Unknown method: $method")}}.onFailure{completion.fail(it.message.orEmpty())}}
 private fun save(id:String,v:Map<String,WireValue>){prefs.edit().putString(id,JSONObject().put("identifier",id).put("title",v.text("title")).put("status",v.text("status")).put("progress",v.decimal("progress")).toString()).apply()}
 private fun post(id:String,v:Map<String,WireValue>){val builder=Notification.Builder(context,"pam_live_activities").setSmallIcon(context.applicationInfo.icon).setContentTitle(v.text("title")).setContentText(v.text("status")).setOngoing(true).setOnlyAlertOnce(true).setCategory(Notification.CATEGORY_PROGRESS).setProgress(100,(v.decimal("progress")*100).toInt().coerceIn(0,100),false);val link=v.text("deepLink");if(link.isNotEmpty())builder.setContentIntent(PendingIntent.getActivity(context,id.hashCode(),Intent(Intent.ACTION_VIEW,Uri.parse(link)).setPackage(context.packageName),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE));manager.notify(id.hashCode(),builder.build())}
 private fun active():JSONArray{val out=JSONArray();prefs.all.values.filterIsInstance<String>().forEach{runCatching{out.put(JSONObject(it))}};return out};private fun Map<String,WireValue>.text(k:String)=(get(k)as?WireValue.Text)?.value.orEmpty();private fun Map<String,WireValue>.decimal(k:String)=when(val v=get(k)){is WireValue.Decimal->v.value;is WireValue.Integer->v.value.toDouble();else->0.0};private fun ModuleCompletion.ok(id:String)=success(mapOf("identifier" to WireValue.Text(id)));private fun ModuleCompletion.success(v:Map<String,WireValue>)=complete(ModuleResultStatus.SUCCESS,WireMap.encode(v));private fun ModuleCompletion.fail(m:String)=complete(ModuleResultStatus.FAILURE,m.take(1024).toByteArray())}
