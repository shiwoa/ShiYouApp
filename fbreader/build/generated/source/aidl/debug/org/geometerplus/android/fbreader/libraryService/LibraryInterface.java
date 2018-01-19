/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\newProject\\AllProject\\ShiYouApp\\fbreader\\src\\org\\geometerplus\\android\\fbreader\\libraryService\\LibraryInterface.aidl
 */
package org.geometerplus.android.fbreader.libraryService;
public interface LibraryInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.geometerplus.android.fbreader.libraryService.LibraryInterface
{
private static final java.lang.String DESCRIPTOR = "org.geometerplus.android.fbreader.libraryService.LibraryInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.geometerplus.android.fbreader.libraryService.LibraryInterface interface,
 * generating a proxy if needed.
 */
public static org.geometerplus.android.fbreader.libraryService.LibraryInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.geometerplus.android.fbreader.libraryService.LibraryInterface))) {
return ((org.geometerplus.android.fbreader.libraryService.LibraryInterface)iin);
}
return new org.geometerplus.android.fbreader.libraryService.LibraryInterface.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_isUpToDate:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isUpToDate();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.geometerplus.android.fbreader.libraryService.LibraryInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public boolean isUpToDate() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isUpToDate, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_isUpToDate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public boolean isUpToDate() throws android.os.RemoteException;
}
