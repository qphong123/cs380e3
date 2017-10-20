import java.net.Socket;

import java.io.InputStream;

import java.io.OutputStream;

import java.io.IOException;



public class Ex3Client {



	public static void main(String[] args) {

		try (Socket socket = new Socket("18.221.102.182", 38103)) {

			System.out.println("Connected to server.");



			InputStream is = socket.getInputStream();

			OutputStream os = socket.getOutputStream();



			int totalNum = is.read();

			System.out.println("Reading " + totalNum + " bytes.");



			byte[] byteArr = new byte[totalNum];

			byte byteValue;

			System.out.println("Data received: ");

			System.out.println("-------------------");



			//format output

			for (int i = 0; i < totalNum; i++) {

				byteValue = (byte) is.read();

				byteArr[i] = byteValue;



				if (i !=0 && i % 10 == 0) {

					System.out.println();

				}

				System.out.printf("%02X", byteArr[i]);

			}



			short get = checkSum(byteArr);

			System.out.println("\n-------------------");

			System.out.print("Checksum calculated: ");

			System.out.printf("0x%02X", get);



			for (int i = 1; i >= 0; i--) {

				os.write(get >> (8 * i));

			}



			int answerCheck = is.read();

			if (answerCheck == 1) {

				System.out.println("\nResponse good.");

			} else {

				System.out.println("\nResponse bad.");

			}



		} catch (IOException e) {

			e.printStackTrace();

		}

	}



	public static short checkSum(byte[] byteArr) {

		int sum = 0;

		int totalArrayNum = byteArr.length - 1;

		int counter = 0;



		while (counter < totalArrayNum) {

			byte firstByte = byteArr[counter];

			byte secondByte = byteArr[counter + 1];



			sum += ((firstByte << 8 & 0xFF00) | (secondByte & 0xFF));



			if ((sum & 0xFFFF0000) > 0) {

				sum &= 0xFFFF;

				sum++;

			}

			counter = counter + 2;

		}



		if ((byteArr.length) % 2 == 1) {

			byte last = byteArr[totalArrayNum];

			sum += ((last << 8) & 0xFF00);



			if ((sum & 0xFFFF0000) > 0) {

				sum &= 0xFFFF;

				sum++;

			}

		}



		return (short) ~(sum & 0xFFFF);

	}

}