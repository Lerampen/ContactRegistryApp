<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, com.registry.contactregistryapp.model.Contact"%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard (Alternative)</title>
    <style>
        /* Base styles */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 20px;
            background-color: #f5f7fa;
            color: #333;
        }
        
        /* Container and sections */
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        
        .card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin-bottom: 20px;
        }
        
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        
        h1, h2, h3 {
            color: #2c3e50;
            margin-top: 0;
        }
        
        /* Debug section */
        .debug-section {
            background-color: #fff8dc;
            border: 1px solid #e6d9a5;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 8px;
        }
        
        .debug-title {
            background: #e6d9a5;
            padding: 5px 10px;
            margin: -15px -15px 15px -15px;
            border-radius: 8px 8px 0 0;
            font-weight: bold;
        }
        
        /* Data display */
        .data-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-box {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 20px;
            text-align: center;
        }
        
        .stat-number {
            font-size: 24px;
            font-weight: bold;
            color: #3498db;
        }
        
        /* Tables */
        table {
            width: 100%;
            border-collapse: collapse;
        }
        
        table th {
            background-color: #f2f6fc;
            padding: 12px 15px;
            text-align: left;
            font-weight: 600;
            border-bottom: 2px solid #ddd;
        }
        
        table td {
            padding: 10px 15px;
            border-bottom: 1px solid #eee;
        }
        
        table tr:hover {
            background-color: #f9f9f9;
        }
        
        /* Charts */
        .chart-row {
            display: flex;
            flex-wrap: wrap;
            margin: 0 -10px;
        }
        
        .chart-container {
            flex: 1;
            min-width: 300px;
            padding: 0 10px;
            margin-bottom: 20px;
        }
        
        /* Error message */
        .error-message {
            background-color: #fee;
            color: #c33;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 5px solid #c33;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Admin Dashboard</h1>
            <div>Last Updated: <%= new java.util.Date() %></div>
        </div>
        
        <!-- DEBUG INFORMATION -->
        <div class="card debug-section">
            <div class="debug-title">DEBUG INFORMATION </div>
            
            <h3>Raw Data Check</h3>
            <% 
                // Get attributes from request
                List<Contact> contacts = (List<Contact>) request.getAttribute("recentContacts");
                Map<String, Integer> genderStats = (Map<String, Integer>) request.getAttribute("genderStats");
                Map<String, Integer> countyStats = (Map<String, Integer>) request.getAttribute("countyStats");
                
                // Check if attributes exist
                boolean hasContacts = contacts != null && !contacts.isEmpty();
                boolean hasGenderStats = genderStats != null && !genderStats.isEmpty();
                boolean hasCountyStats = countyStats != null && !countyStats.isEmpty();
            %>
            
            <ul>
                <li>Recent Contacts: <%= contacts != null ? contacts.size() + " records found" : "NULL" %></li>
                <li>Gender Stats: <%= genderStats != null ? genderStats.size() + " categories found" : "NULL" %></li>
                <li>County Stats: <%= countyStats != null ? countyStats.size() + " counties found" : "NULL" %></li>
            </ul>
            
            <% if (hasGenderStats) { %>
                <h4>Raw Gender Data:</h4>
                <table>
                    <tr>
                        <th>Gender</th>
                        <th>Count</th>
                    </tr>
                    <% for (Map.Entry<String, Integer> entry : genderStats.entrySet()) { %>
                        <tr>
                            <td><%= entry.getKey() %></td>
                            <td><%= entry.getValue() %></td>
                        </tr>
                    <% } %>
                </table>
            <% } %>
            
            <% if (hasCountyStats) { %>
                <h4>Raw County Data:</h4>
                <table>
                    <tr>
                        <th>County</th>
                        <th>Count</th>
                    </tr>
                    <% for (Map.Entry<String, Integer> entry : countyStats.entrySet()) { %>
                        <tr>
                            <td><%= entry.getKey() %></td>
                            <td><%= entry.getValue() %></td>
                        </tr>
                    <% } %>
                </table>
            <% } %>
        </div>
        
        <!-- ERROR MESSAGE (if any) -->
        <% 
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
            <div class="error-message">
                <strong>Error:</strong> <%= errorMessage %>
            </div>
        <% } %>
        
        <!-- SUMMARY STATS -->
        <div class="card">
            <h2>Summary Statistics</h2>
            <div class="data-grid">
                <div class="stat-box">
                    <h3>Total Contacts</h3>
                    <div class="stat-number">
                        <% 
                            int totalContacts = 0;
                            if (hasGenderStats) {
                                for (Integer count : genderStats.values()) {
                                    totalContacts += count;
                                }
                            }
                            out.print(totalContacts);
                        %>
                    </div>
                </div>
                
                <div class="stat-box">
                    <h3>Unique Counties</h3>
                    <div class="stat-number">
                        <%= hasCountyStats ? countyStats.size() : 0 %>
                    </div>
                </div>
                
                <div class="stat-box">
                    <h3>Gender Categories</h3>
                    <div class="stat-number">
                        <%= hasGenderStats ? genderStats.size() : 0 %>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- GENDER DISTRIBUTION -->
        <div class="card">
            <h2>Gender Distribution</h2>
            <% if (hasGenderStats) { %>
                <table>
                    <tr>
                        <th>Gender</th>
                        <th>Count</th>
                        <th>Percentage</th>
                        <th>Visual</th>
                    </tr>
                    <% 
                        int total = 0;
                        for (Integer value : genderStats.values()) {
                            total += value;
                        }
                        
                        for (Map.Entry<String, Integer> entry : genderStats.entrySet()) {
                            String gender = entry.getKey();
                            int count = entry.getValue();
                            double percentage = (total > 0) ? (count * 100.0 / total) : 0;
                            int barWidth = (int) (percentage * 2); // Scale the bar width
                    %>
                        <tr>
                            <td><strong><%= gender %></strong></td>
                            <td><%= count %></td>
                            <td><%= String.format("%.1f%%", percentage) %></td>
                            <td>
                                <div style="background-color: #3498db; height: 20px; width: <%= barWidth %>px;"></div>
                            </td>
                        </tr>
                    <% } %>
                </table>
            <% } else { %>
                <p>No gender data available.</p>
            <% } %>
        </div>
        
        <!-- COUNTY DISTRIBUTION -->
        <div class="card">
            <h2>County Distribution</h2>
            <% if (hasCountyStats) { %>
                <table>
                    <tr>
                        <th>County</th>
                        <th>Count</th>
                        <th>Percentage</th>
                        <th>Visual</th>
                    </tr>
                    <% 
                        int total = 0;
                        for (Integer value : countyStats.values()) {
                            total += value;
                        }
                        
                        for (Map.Entry<String, Integer> entry : countyStats.entrySet()) {
                            String county = entry.getKey();
                            int count = entry.getValue();
                            double percentage = (total > 0) ? (count * 100.0 / total) : 0;
                            int barWidth = (int) (percentage * 2); // Scale the bar width
                    %>
                        <tr>
                            <td><strong><%= county %></strong></td>
                            <td><%= count %></td>
                            <td><%= String.format("%.1f%%", percentage) %></td>
                            <td>
                                <div style="background-color: #e67e22; height: 20px; width: <%= barWidth %>px;"></div>
                            </td>
                        </tr>
                    <% } %>
                </table>
            <% } else { %>
                <p>No county data available.</p>
            <% } %>
        </div>
        
        <!-- RECENT CONTACTS -->
        <div class="card">
            <h2>Recently Added Contacts</h2>
            <% if (hasContacts) { %>
                <table>
                    <thead>
                        <tr>
                            <th>Full Name</th>
                            <th>Phone</th>
                            <th>Email</th>
                            <th>County</th>
                            <th>Date Created</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Contact c : contacts) { %>
                            <tr>
                                <td><%= c.getFullName() %></td>
                                <td><%= c.getPhoneNumber() %></td>
                                <td><%= c.getEmail() %></td>
                                <td><%= c.getCountyOfResidence() %></td>
                                <td><%= c.getCreatedAt() %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p>No recent contacts available.</p>
            <% } %>
        </div>
    </div>
</body>
</html>