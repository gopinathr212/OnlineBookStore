package com.edubrige.miniProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

public class OnlineBookStore {

	public static void main(String[] args) throws NumberFormatException, IOException, SQLException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("==============================================================================");
		System.out.println("=============================== WELCOME TO EBook =============================");
		System.out.println("==============================================================================");
		System.out.println("1  --->   Buyer");
		System.out.println("2  --->   Seller");
		System.out.println("3  --->   Sign Up");
		System.out.println("------------------------------------------------------------------------------");
		System.out.print("\t\t Tell us Who you are?");
		int choice = Integer.parseInt(br.readLine());
        //Buyer section
		if (choice == 1) {
			System.out.println("==============================================================================");
			System.out.println("==========================  ENTER LOGIN DETAILS ==============================");
			System.out.print("\t\t Enter your username:");
			String userName = br.readLine();
			System.out.print("\t\t Enter your password:");
			String passWord = br.readLine();
			System.out.println("==============================================================================");
			Connection conn = MySqlConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from buyer where userName=?");
			ps.setString(1, userName);
			ResultSet result = ps.executeQuery();
			String userPassWord = null;
			int userId = 0;
			String add = "null";
			while (result.next()) {
				userPassWord = result.getString("passWord");
				userId = result.getInt("buyerId");
				add = result.getString("address");
			}

			if (passWord.equals(userPassWord)) {

				System.out.println("You have successfully logged in!!");

				boolean login = true;
				do {

					System.out
							.println("==============================================================================");
					System.out.println("==========================  WELCOME " + userName.toUpperCase()
							+ " ==============================");
					System.out
							.println("==============================================================================");
					System.out.println("1. Show Books\n2. Search Books\n3. Order\n4. LogOut");
					System.out
							.println("==============================================================================");
					System.out.print("\t\t Enter your choice:");
					int operationNumber = Integer.parseInt(br.readLine());
					System.out
							.println("==============================================================================");
					String status = null;
					switch (operationNumber) {
					//Showing the books which has qunatity more than zero
					case 1:
						System.out.println("Show Books");
						try {
							ps = conn.prepareStatement("SELECT * FROM books where quantity>0");
							result = ps.executeQuery();
							if (result.next()) {
								do {
									bookDetails(result);
								} while (result.next());
							} else {
								System.out.println("No books found");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println("Do you want to continue??(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login = false;
						}
						break;
					//book filter using name,genre,author
					case 2:
						System.out.println("Book Search filter");
						System.out.println("11. Book Name\n12. Book Genre\n13. Author");
						System.out.println(
								"==============================================================================");
						System.out.print("\t\t Enter your filter choice:");
						int filterNum = Integer.parseInt(br.readLine());
						System.out.println(
								"==============================================================================");
						System.out.println("Search Books");
						//filter using bookname
						if (filterNum == 11) {
							System.out.print("Enter book name: ");
							String bookName = br.readLine();
							try {
								ps = conn.prepareStatement("SELECT * FROM books WHERE book_name=? and quantity>0");
								ps.setString(1, bookName);
								result = ps.executeQuery();
								if (result.next()) {
									do {
										bookDetails(result);
									} while (result.next());
								} else {
									System.out.println("No books found");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						}
						//filter using BookGenre
						else if (filterNum == 12) {
							System.out.print("Enter book Genre: ");
							String bookGenre = br.readLine();
							try {
								ps = conn.prepareStatement("SELECT * FROM books WHERE book_type=? and quantity>0");
								ps.setString(1, bookGenre);
								result = ps.executeQuery();
								if (result.next()) {
									do {
										bookDetails(result);
									} while (result.next());
								} else {
									System.out.println("No books found");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						} 
						//filter using AuthorName
						else if (filterNum == 13) {
							System.out.print("Enter Author Name: ");
							String AuthorName = br.readLine();
							try {
								ps = conn.prepareStatement("SELECT * FROM books WHERE author=? and quantity>0");
								ps.setString(1, AuthorName);
								result = ps.executeQuery();
								if (result.next()) {
									do {
										bookDetails(result);
									} while (result.next());
								} else {
									System.out.println("No books found");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						} else {
							System.out.println("Invalid choice");
						}
						System.out.println("Do you want to continue??(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login = false;
						}
						//Ordering the book which is needed
					case 3:
						System.out.println("Book Your Orders");
						System.out.println("Enter Book Name:");
						String bookName = br.readLine();
						System.out.println("Enter Book Quantity:");
						int quantity = Integer.parseInt(br.readLine());
						if (quantity > 0) {
							ps = conn.prepareStatement("select * from books where book_name=? and quantity>0 ");
							ps.setString(1, bookName);
							result = ps.executeQuery();

							int quantityBook = 0;
							String bookName1 = null;
							int bookId = 0;
							int shopId = 0;
							while (result.next()) {
								quantityBook = result.getInt("quantity");
								bookName1 = result.getString("book_name");
								bookId = result.getInt("book_id");
								shopId = result.getInt("shop_id");
							}
							LocalDate newDate1 = LocalDate.now().plusDays(3);

							if (quantityBook > quantity) {
								quantityBook = quantityBook - quantity;
								ps = conn.prepareStatement("update books set quantity=? where book_name=?");
								ps.setInt(1, quantityBook);
								ps.setString(2, bookName1);

								if (ps.executeUpdate() > 0) {
									System.out.println("inside delivey");
									ps = conn.prepareStatement("insert into delivery values(?,?,?,?,?,?,?,?)");
									Timestamp timestamp = new Timestamp(System.currentTimeMillis());
									String deliveryId = "TN" + timestamp.getTime(); // TN3243432432423
									ps.setInt(1, bookId);
									ps.setString(2, deliveryId);
									ps.setInt(3, userId);
									ps.setInt(4, quantityBook);
									ps.setString(5, add);
									ps.setLong(6, shopId);
									ps.setDate(7, new Date(System.currentTimeMillis()));
									ps.setDate(8, Date.valueOf(newDate1));
									ps.executeUpdate();

									System.out.println("Balance Updated!!");
								}

								else {
									System.out.println("Something went wrong!!");
								}
							} else {

								System.out.println("Insufficient Book Stock!!");
							}
						}
						System.out.println("Do you want to continue??(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login = false;
						}
						break;
					case 4:
						System.out.println("Thank you for using our service");
						login = false;
						break;
					default:
						System.out.println("Invalid choice");
						break;
					}
				} while (login);
				{
				}
			} else {
				System.out.println("Invalid password");
			}
		} 
		//seller section
		else if (choice == 2) {
			System.out.println("==============================================================================");
			System.out.println("==========================  ENTER LOGIN DETAILS ==============================");
			System.out.print("\t\t Enter your username:");
			String sell_userName = br.readLine();
			System.out.print("\t\t Enter your password:");
			String passWord = br.readLine();
			System.out.println("==============================================================================");
			Connection conn = MySqlConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from seller where sell_userName=?");
			ps.setString(1, sell_userName);
			ResultSet result = ps.executeQuery();
			String userPassWord = null;

			while (result.next()) {
				userPassWord = result.getString("passWord");
			}

			if (passWord.equals(userPassWord)) {

				System.out.println("You have successfully logged in!!");
				boolean login1 = true;
				do {

					System.out
							.println("==============================================================================");
					System.out.println("==========================  WELCOME " + sell_userName.toUpperCase()
							+ " ==============================");
					System.out
							.println("==============================================================================");
					System.out.println("1. Show Books\n2. Add Books\n3. Delete Book\n4. LogOut");
					System.out
							.println("==============================================================================");
					System.out.print("\t\t Enter your choice:");
					int sellerChoice = Integer.parseInt(br.readLine());
					System.out
							.println("==============================================================================");
					String status = null;
					switch (sellerChoice) {
						//Showing the every books
					case 1:
						System.out.println("Show Books");
						try {
							ps = conn.prepareStatement("SELECT * FROM books");
							result = ps.executeQuery();
							if (result.next()) {
								do {
									bookDetails(result);
								} while (result.next());
							} else {
								System.out.println("No books found");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println("Do you want to continue??(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login1 = false;
						}
						break;
                     //add new books 
					case 2:
						System.out.println("Add Books");
						System.out.println("Enter Book name:");
						String BName = br.readLine();

						System.out.println("Enter Author name");
						String AuName = br.readLine();

						System.out.println("Enter Book Id:");
						int bookId = Integer.parseInt(br.readLine());

						System.out.println("Enter Price:");
						int price = Integer.parseInt(br.readLine());

						System.out.println("Enter quantity:");
						int quantity = Integer.parseInt(br.readLine());

						System.out.println("Enter bookType:");
						String bookType = br.readLine();

						ps = conn.prepareStatement("insert into books values (?, ?, ?, ?, ?, ?)");
						ps.setInt(1, bookId);
						ps.setString(2, BName);
						ps.setInt(3, price);
						ps.setInt(4, quantity);
						ps.setString(5, bookType);
						ps.setString(6, AuName);

						if (ps.executeUpdate() > 0) {
							System.out.println(
									"==============================================================================");
							System.out.println("New Book added successfully!!");
							System.out.println(
									"==============================================================================");

						} else {
							System.out.println(
									"==============================================================================");
							System.out.println("Problem in adding Book!!");
							System.out.println(
									"==============================================================================");
						}
						System.out.println("Do you want to continue??(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login1 = false;
						}
						break;
                     //deleting the books
					case 3:
						System.out.println("Delete  Books");
						System.out.println("Enter Book Id:");
						int delBookId = Integer.parseInt(br.readLine());
						ps = conn.prepareStatement("delete from books where book_id =?");
						ps.setInt(1, delBookId);
						if (ps.executeUpdate() > 0) {
							System.out.println(
									"==============================================================================");
							System.out.println("Account closed successfully!!");
							System.out.println(
									"==============================================================================");
						} else {
							System.out.println(
									"==============================================================================");
							System.out.println("Account id does not exist!!");
							System.out.println(
									"==============================================================================");
						}
						System.out.println("Do you want to continue??(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login1 = false;
						}
						break;
					case 4:
						System.out.println("Okay bye " + sell_userName.toUpperCase());
						login1 = false;
						break;
					default:
						System.out.println("Invalid choice");
						break;
					}
				} while (login1);
			}
		}
		//Creating new buyer user
		else if (choice == 3) {
			System.out.println("Welcome to EBook");
			System.out.println("We are to create an new user Account");
			System.out.println("Enter your User name:");
			String uName = br.readLine();

			System.out.println("Enter your user Id:");
			int uId = Integer.parseInt(br.readLine());

			System.out.println("Enter your Password:");
			String passWORD = br.readLine();

			System.out.println("Enter your Address:");
			String address = br.readLine();

			System.out.println("Enter your Phone Number:");
			Long phNum = Long.parseLong(br.readLine());
			Connection conn = MySqlConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from buyer ");
			ResultSet result = ps.executeQuery();
			ps = conn.prepareStatement("insert into buyer values (?, ?, ?, ?, ?)");
			ps.setInt(1, uId);
			ps.setString(2, uName);
			ps.setString(3, passWORD);
			ps.setString(4, address);
			ps.setLong(5, phNum);

			if (ps.executeUpdate() > 0) {
				System.out.println("==============================================================================");
				System.out.println("User Account Created successfully!!");
				System.out.println("==============================================================================");

			} else {
				System.out.println("==============================================================================");
				System.out.println("Problem in creating user account!!");
				System.out.println("==============================================================================");
			}
		} else {
			System.out.println("Invalid Choice");
		}

	}

	private static void bookDetails(ResultSet result) throws SQLException {
		System.out.println("Book Id: " + result.getInt("book_id"));
		System.out.println("Book Name: " + result.getString("book_name"));
		System.out.println("Price: " + result.getDouble("price"));
		System.out.println("Author: " + result.getString("author"));
		System.out.println("Quantity: " + result.getInt("quantity"));
		System.out.println("Book Type: " + result.getString("book_type"));
		System.out.println("--------------------------------------");
	}
}
