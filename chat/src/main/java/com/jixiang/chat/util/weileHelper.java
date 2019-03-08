package com.jixiang.chat.util;

import java.util.Random;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class weileHelper {
	/**
	*@brief 当前包不需要做其他处理，直接发送原始数据
	*/
	static public final int MSG_HEADER_FLAG_NONE = 0;
	///当前为分包文件中的一部分，接收后需拼接（一个或者多个连续包）
	static public final int MSG_HEADER_FLAG_PACKET		= 0x1;	
	///消息包需要异或运算并校验掩码，处理完后校验码存储与  msg_Header::byMask
	static public final int MSG_HEADER_FLAG_MASK		= 0x2;
	///重新编码/解码根据编码表/解码表重新填充数据
	static public final int MSG_HEADER_FLAG_ENCODE		= 0x4;	
	/**
	* @brief 压缩数据.
	*发送时该标记表示可以压缩数据，如果压缩后数据大于压缩前，则自动去掉该标记；
	*接收时该标记表示数据经过了压缩，需要解压处理
	*/
	static public final int MSG_HEADER_FLAG_COMPRESS	= 0x8;	
	/**
	* @brief 路由包.
	* 每次服务器中转都会累加一次  msg_Header::byRouteCount ,当  msg_Header::byRouteCount 大于或者等于  
	*  MAX_MSG_ROUTE_COUNT 时，自动丢弃消息包
	*/
	static public final int MSG_HEADER_FLAG_ROUTE		= 0x10;
	/**
	*延迟消息，等待下次非延迟消息合并发送，调用 SendData 空消息可立即发送延迟的消息
	*/
	static public final int MSG_HEADER_FLAG_DELAYSEND	= 0x20;
	/**
	* @brief 已经经过了处理（压缩、加密、异或操作等）并且  msg_Header 的所有参数都已经设置好了，发送时直接发送.
	* @note  MSG_HEADER_FLAG_NONE 和该标记都是直接发送数据，该标记与前者不同的是，前者需要重新填充  msg_Header 。
	* 该标记还会忽略除了  MSG_HEADER_FLAG_DELAYSEND 外的所有标记
	*/
	static public final int MSG_HEADER_FLAG_FILLED		= 0x40;
	/**
	* @brief 指出开发者指定了消息头偏移大小（设置了  msg_Header::byHeaderOffset 的值）  	
	*/
	static public final int MSG_HEADER_FLAG_OFFSET		= 0x80;
	
	static private int m_Encode_Table[]={
		205,240,162,226,237,136,227,152,	188,102,193,124,244,219, 71,150,
		246,108, 94, 17,175, 47, 64, 66,	 65,  7,222, 76,138, 99, 77, 81,
		192,155, 56, 39, 25,  3,151,101,	 61, 68,172,167, 24,160, 97, 19,
		179,180,198, 33, 21,224,197, 15,	120,196,239, 44, 83, 38, 46,103,
		 84, 95,213,200,170, 23, 70,149,	163,148,251,186,211,189,100, 42,
		191, 52, 72, 53, 67,215,245,207,	144,146, 45,181, 93,147,153, 80,
		116,114, 49,  4, 88, 16, 90,127,	255,202, 85, 55,178,221,229, 10,
		13,105,177, 58,  0, 60,234, 34,		 50,141,242,156,134, 28,176,118,
		 48,  1,210,  6,187,119,243,128,	232,166,  5,236,137, 73,253,217,
		214,212, 69,111, 79,184, 51, 87,	216,135,168,158,249, 92, 35,182,
		107,235,126, 31,  2,254,133,233,	 18,201,174,  8,159, 82, 37,113,
		  9, 63, 41,104, 59, 26,231,145,	 89,122,110,142, 86,164, 29, 30,
		169,154,223,112,140, 78, 75,220,	238, 54,143,195,131,130,228,139,
		121,109, 12,161,125, 57, 74,190,	250,171,209,199, 40,123,204,247,
		183,173, 98,185,208,248,241, 14,	 27,203,218,230, 43, 96, 22,194,
		129, 11,115,165, 32,132, 91, 36,	157,117,225,206, 20,106, 62,252,
	};
	
	static private int m_Decode_Table[]=	{
		139,126, 91,218,156,117,124,230,	 84, 79,144, 14, 45,143, 24,200,
		154,236, 87,208,  3,203, 17,186,	211,219, 74, 23,130, 65, 64, 92,
		 11,204,136, 97,  8, 81,194,220,	 35, 77,176, 19,196,165,193,234,
		127,157,135,105,174,172, 54,148,	221, 42,140, 75,138,215,  1, 78,
		233,231,232,171,214,109,185,241,	173,114, 41, 57,228,225, 58,107,
		160,224, 82,195,191,149, 67,104,	155, 71,153,  9, 98,163,237,190,
		 18,209, 29,226,177,216,246,192,	 76,142,  2, 95,238, 46, 69,108,
		 60, 80,158, 13,159,  6,128,122,	199, 47, 70, 34,244, 43, 93,152,
		120, 15, 50, 51, 10, 89,131,102,	250,115,227, 48, 59,134, 68, 53,
		167, 72,166,162,182,184,240,217,	248,161, 62,222,132,  7,100, 83,
		210, 44,253,183, 66, 12,118,212,	101, 63,187, 38,213, 30, 85,235,
		129,141,147,207,206,164, 96, 31,	106, 28,180,123,247,178, 40,175,

		223,245, 16, 52,198,201,205, 36,	188, 86,150, 22, 33,255,  4,168,
		 27, 37,125,179,110,189,111,170,	103,112, 21,242, 56,146,229, 61,
		202,  5,252,249, 49,145, 20, 73,	119, 88,137, 94,116,251, 55,197,
		254, 25,133,121,243,169,239, 32,	 26, 99, 39,181,  0,113, 90,151,

	};
	static private int Byte2Int(byte d)
	{
		return d>=0?d:256+d;
	}

	static public int MakeWord(byte l,byte h)
	{
		return Byte2Int(l)|(Byte2Int(h)<<8);
	}
	
	static public void MakeWord(int nVal,byte []data,int nStart)
	{
		data[nStart]=(byte)nVal;
		data[nStart+1]=(byte)(nVal>>8);
	}
	static public void MakeWord(int nVal,byte []data)
	{
		data[0]=(byte)nVal;
		data[1]=(byte)(nVal>>8);
	}
	/**
	* @brief 数据包编码预处理.
	* 当发送数据包时，调用该函数进行压缩、加密、异或编码等操作并重新填写消息头
	* @param [in] data 需要处理的消息头
	* @param [in] nStart data中数据的起始位置
	* @param [in] nLen 数据的长度,单位:字节
	* @return 返回处理完毕后的数据,如果返回null,则失败
	*/
	static public byte[] EscapeMessage(byte []data,int nStart,int nLen)
	{
		if(nLen<8)
			return null;
		
		int nMsgLen = MakeWord(data[nStart],data[nStart+1]);
		int nFlag = Byte2Int(data[nStart+5]);
		int nMsgOffset= Byte2Int(data[nStart+7]);
		if((nFlag &MSG_HEADER_FLAG_OFFSET)==0)//没有指定偏移
			nMsgOffset = 8;  
		else if(nMsgOffset<8)
			return null;
		
		nMsgLen-=nMsgOffset;
		byte []newData = null;
		if(nMsgLen>0)
		{
			int nOffset = nStart+nMsgOffset;
			if((nFlag & MSG_HEADER_FLAG_COMPRESS)!=0)//压缩
			{
				byte []buff= new byte[nMsgLen];
				Deflater zlib = new Deflater();
				zlib.reset();
				zlib.setInput(data, nOffset, nMsgLen);
				zlib.finish();
				
				int nUsed=zlib.deflate(buff);
				zlib.end();
				if(nUsed==0)//为0则缓冲不够,压缩后比原始数据大?
					nFlag ^=MSG_HEADER_FLAG_COMPRESS;//取消压缩
				else
				{
					newData = new byte[nMsgOffset+nUsed];
					System.arraycopy(data, nStart, newData, 0, nMsgOffset);
					System.arraycopy(buff, 0, newData, nMsgOffset, nUsed);
					nMsgLen = nUsed;
				}	
			}

			if(newData == null)
			{
				newData = new byte[nLen];
				System.arraycopy(data, nStart, newData, 0, nLen);
			}
			
			if((nFlag&MSG_HEADER_FLAG_ENCODE)!=0)//需要混淆
			{
				for(int i=0;i<nMsgLen;++i)
					newData[nMsgOffset+i] = (byte) m_Encode_Table[Byte2Int(newData[nMsgOffset+i])];
			}
			
			if((nFlag &MSG_HEADER_FLAG_MASK)!=0)//需要校验
			{
				Random  random = new Random ();
				byte cbMask = (byte)random.nextInt();
				for(int i=0;i<nMsgLen;++i)
				{
					newData[nMsgOffset+i]^=cbMask;
					cbMask^=(byte)i;
				}
		
				newData[4] = cbMask;
			}
		}
		else
		{
			newData = new byte[nLen];
			System.arraycopy(data, nStart, newData, 0, nLen);
		}
		nMsgLen+=nMsgOffset;
		nFlag |=MSG_HEADER_FLAG_FILLED;
		newData[0]=(byte)nMsgLen;
		newData[1]=(byte)(nMsgLen/256);
		newData[2] = data[nStart+2];
		newData[3] = data[nStart+3];
		newData[5] = (byte)nFlag;
		newData[6] = data[nStart+6];
		newData[7] = (byte)nMsgOffset;
		return newData;
	}
	/**
	* @brief 数据包编码预处理.
	* 当发送数据包时，调用该函数进行压缩、加密、异或编码等操作并重新填写消息头
	* @param [in] data 需要处理的消息头
	* @return 返回处理完毕后的数据,如果返回null,则失败
	*/
	static public byte[] EscapeMessage(byte []data)
	{
		return EscapeMessage(data,0,data.length);
	}
	
	/**
	* @brief 数据包解码预处理.
	* 该函数为 EscapeMessage 的反操作函数，收到数据后，调用该函数进行异或编码、解密、解压等操作并重新填写消息头
	* @param [in,out] data 需要处理的消息头,可能修改数据内容
	* @param [in] nStart data中数据的起始位置
	* @param [in] nLen 数据的长度,单位:字节
	* @return 返回处理完毕后的数据,如果返回null,则失败
	*/
	static public byte[] UnEscapeMessage(byte []data,int nStart,int nLen)
	{
		
		int nFlag = data[nStart+5];
		int nOffset = data[nStart+7];
		if((nFlag& MSG_HEADER_FLAG_FILLED)==0)
		{
			byte []newData = new byte[nLen];
			System.arraycopy(data, nStart, newData, 0, nLen);
			return newData;
		}
		if(nOffset<8)
			return null;
		int nMsgLen = MakeWord(data[nStart],data[nStart+1])-nOffset;
		
		byte []newData = null;
		if(nMsgLen>0)
		{
			if((nFlag& MSG_HEADER_FLAG_MASK)!=0)
			{
				byte cbMask = data[nStart+4];
				for(int i=nMsgLen-1;i>=0;--i)
				{
					cbMask^=(byte)i;
					data[nStart+i+nOffset]^=cbMask;
				}
			}
			
			if((nFlag & MSG_HEADER_FLAG_ENCODE)!=0)
			{
				for(int i=0;i<nMsgLen;++i)
					data[nStart+i+nOffset] = (byte) ~m_Decode_Table[Byte2Int(data[nStart+i+nOffset])];
			}
			if((nFlag & MSG_HEADER_FLAG_COMPRESS)!=0)
			{
				byte []buff= new byte[8192];
				Inflater zlib = new Inflater();
				zlib.reset();
				zlib.setInput(data, nOffset, nMsgLen);
				zlib.finished();
				
				int nUsed;
				try {
					nUsed = zlib.inflate(buff);
				} catch (DataFormatException e) {
					return null;
				}
				zlib.end();
				if(nUsed==0)
					return null;
				else
				{
					newData = new byte[nOffset+nUsed];
					System.arraycopy(data, nStart, newData, 0, nOffset);
					System.arraycopy(buff, 0, newData, nOffset, nUsed);
					nMsgLen = nUsed;
					newData[0]=(byte)nMsgLen;
					newData[1]=(byte)(nMsgLen/256);
				}	
			}
		}
		if(newData == null)
		{
			newData = new byte[nLen];
			System.arraycopy(data, nStart, newData, 0, nLen);
		}
		nFlag &=~MSG_HEADER_FLAG_FILLED;
		newData[5] = (byte)nFlag;
		return newData;
	}
	
	/**
	* @brief 数据包解码预处理.
	* 该函数为 EscapeMessage 的反操作函数，收到数据后，调用该函数进行异或编码、解密、解压等操作并重新填写消息头
	* @param [in,out] data 需要处理的消息头,可能修改数据内容
	* @return 返回处理完毕后的数据,如果返回null,则失败
	*/
	static public byte[] UnEscapeMessage(byte []data)
	{
		return UnEscapeMessage(data,0,data.length);
	}
}
