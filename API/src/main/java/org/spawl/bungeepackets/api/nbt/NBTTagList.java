package org.spawl.bungeepackets.api.nbt;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;

@EqualsAndHashCode(callSuper = false)
public class NBTTagList extends NBTBase implements List<NBTBase>
{
	public static final NBTType TYPE = NBTType.LIST;
	@Delegate(excludes = {Delegator.class})
	private final List<NBTBase> list = new ArrayList<>();
	@SuppressWarnings("FieldNotUsedInToString")
	@Getter
	private NBTType dataType;

	@Override
	void write(DataOutput out)
			throws IOException
	{
		out.writeByte(list.isEmpty() ? 0 : dataType.getID());
		out.writeInt(list.size());
		for (NBTBase entry : list)
			entry.write(out);
	}

	@Override
	void load(DataInput in, int depth, NBTReadLimiter limiter)
			throws IOException
	{
		if (depth > 512)
			throw new NBTException("Tried to increment NBT tag with too high complexity, depth > 512");

		limiter.increment(1);

		byte id = in.readByte();
		dataType = NBTType.get(id);
		if (dataType == null)
			throw new NBTException("Cannot find NBTType with windowID " + id);

		int size = in.readInt();


		for (int j = 0; j < size; j++)
		{
			NBTBase nbt = dataType.newInstance();

			//noinspection ConstantConditions
			nbt.load(in, depth + 1, limiter);
			list.add(nbt);
		}
	}

	private void safeAdd(NBTBase nbt)
	{
		if (dataType == null)
			dataType = nbt.getType();
		else if (dataType != nbt.getType())
			throw new IllegalArgumentException("Cannot add an object with type " + nbt.getType() + " (" +
					nbt.getTypeId() + ") into a list of type " + dataType + " (" + dataType.getID() + ')');
	}

	@Override
	public NBTType getType()
	{
		return TYPE;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("[");
		int i = 0;
		for (NBTBase nbt : list)
		{
			builder.append(i)
					.append(':')
					.append(nbt)
					.append(',');
			i++;
		}
		return builder.append(']').toString();
	}

	@Override
	public boolean add(NBTBase nbt)
	{
		safeAdd(nbt);
		return list.add(nbt);
	}

	@Override
	public boolean addAll(Collection<? extends NBTBase> collection)
	{
		collection.forEach(this::safeAdd);
		return list.addAll(collection);
	}

	@Override
	public boolean addAll(int i, Collection<? extends NBTBase> collection)
	{
		collection.forEach(this::safeAdd);
		return list.addAll(i, collection);
	}

	@Override
	public ListIterator<NBTBase> listIterator()
	{
		return new Itr();
	}

	@Override
	public ListIterator<NBTBase> listIterator(int i)
	{
		return new Itr(i);
	}

	@Override
	public List<NBTBase> subList(int i, int i1)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Iterator<NBTBase> iterator()
	{
		return new Itr();
	}

	@Override
	public void clear()
	{
		list.clear();
		dataType = null;
	}

	@Override
	public NBTBase set(int i, NBTBase nbt)
	{
		safeAdd(nbt);
		return list.set(i, nbt);
	}

	@Override
	public void add(int i, NBTBase nbt)
	{
		safeAdd(nbt);
		list.add(i, nbt);
	}

	public NBTBase get(int index, NBTBase defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue : nbt;
	}

	public Byte getByte(int index)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? null :
				(nbt.getType() == NBTType.BYTE ? ((NBTTagByte) nbt).getValue() : null);
	}

	public byte getByte(int index, byte defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.BYTE ? ((NBTTagByte) nbt).getValue() : defaultValue);
	}

	public Short getShort(int index)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? null :
				(nbt.getType() == NBTType.SHORT ? ((NBTTagShort) nbt).getValue() : null);
	}

	public short getShort(int index, short defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.SHORT ? ((NBTTagShort) nbt).getValue() : defaultValue);
	}

	public Integer getInt(int index)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? null :
				(nbt.getType() == NBTType.INT ? ((NBTTagInt) nbt).getValue() : null);
	}

	public int getInt(int index, int defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.INT ? ((NBTTagInt) nbt).getValue() : defaultValue);
	}

	public Long getLong(int index)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? null :
				(nbt.getType() == NBTType.LONG ? ((NBTTagLong) nbt).getValue() : null);
	}

	public long getLong(int index, long defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.LONG ? ((NBTTagLong) nbt).getValue() : defaultValue);
	}

	public Float getFloat(int index)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? null :
				(nbt.getType() == NBTType.FLOAT ? ((NBTTagFloat) nbt).getValue() : null);
	}

	public float getFloat(int index, float defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.FLOAT ? ((NBTTagFloat) nbt).getValue() : defaultValue);
	}

	public Double getDouble(int index)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? null :
				(nbt.getType() == NBTType.DOUBLE ? ((NBTTagDouble) nbt).getValue() : null);
	}

	public double getDouble(int index, double defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.DOUBLE ? ((NBTTagDouble) nbt).getValue() : defaultValue);
	}

	public byte[] getByteArray(int index)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? null :
				(nbt.getType() == NBTType.BYTE_ARRAY ? ((NBTTagByteArray) nbt).getValue() : null);
	}

	public byte[] getByteArray(int index, byte[] defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.BYTE_ARRAY ? ((NBTTagByteArray) nbt).getValue() : defaultValue);
	}

	public String getString(int index)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? null :
				(nbt.getType() == NBTType.STRING ? ((NBTTagString) nbt).getValue() : null);
	}

	public String getString(int index, String defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.STRING ? ((NBTTagString) nbt).getValue() : defaultValue);
	}

	public NBTTagList getList(int index)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? null :
				(nbt.getType() == NBTType.LIST ? (NBTTagList) nbt : null);
	}

	public NBTTagList getList(int index, NBTTagList defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.LIST ? (NBTTagList) nbt : defaultValue);
	}

	public NBTTagCompound getCompound(int index)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? null :
				(nbt.getType() == NBTType.COMPOUND ? (NBTTagCompound) nbt : null);
	}

	public NBTTagCompound getCompound(int index, NBTTagCompound defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.COMPOUND ? (NBTTagCompound) nbt : defaultValue);
	}

	public int[] getIntArray(int index)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? null :
				(nbt.getType() == NBTType.INT_ARRAY ? ((NBTTagIntArray) nbt).getValue() : null);
	}

	public int[] getIntArray(int index, int[] defaultValue)
	{
		NBTBase nbt = list.get(index);
		return nbt == null ? defaultValue :
				(nbt.getType() == NBTType.INT_ARRAY ? ((NBTTagIntArray) nbt).getValue() : defaultValue);
	}

	public Boolean getBoolean(int index)
	{
		Byte b = getByte(index);
		return b == null ? null : b != 0;
	}

	public boolean getBoolean(int index, boolean defaultValue)
	{
		Byte b = getByte(index);
		return b == null ? defaultValue : b != 0;
	}

	private class Itr implements ListIterator<NBTBase>
	{
		@Delegate(excludes = {ItrDelegator.class})
		ListIterator<NBTBase> iterator;

		Itr()
		{
			iterator = list.listIterator();
		}

		Itr(int i)
		{
			iterator = list.listIterator(i);
		}

		@Override
		public void set(NBTBase nbt)
		{
			safeAdd(nbt);
			iterator.set(nbt);
		}

		@Override
		public void add(NBTBase nbt)
		{
			safeAdd(nbt);
			iterator.add(nbt);
		}
	}

	private interface Delegator
	{
		void clear();

		Iterator<NBTBase> iterator();

		boolean add(NBTBase nbtBase);

		boolean addAll(Collection<? extends NBTBase> collection);

		boolean addAll(int i, Collection<? extends NBTBase> collection);

		void replaceAll(UnaryOperator<NBTBase> unaryOperator); //todo

		NBTBase set(int i, NBTBase nbtBase);

		void add(int i, NBTBase nbtBase);

		ListIterator<NBTBase> listIterator();

		ListIterator<NBTBase> listIterator(int i);

		List<NBTBase> subList(int i, int i1); //todo
	}

	private interface ItrDelegator
	{
		void set(NBTBase nbt);

		void add(NBTBase nbt);
	}
}