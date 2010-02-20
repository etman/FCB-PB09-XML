<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<style type="text/css">
			table, table tr th, table tr td{
			border:1px solid
			#000000
			}
	    </style>
		<h2>PB09 Report</h2>
		<table border="0" cellspacing="0" cellpadding="0" width="100%"
			style="font-size:10pt">
			<tr bgcolor="#9acd32">
				<th>單位名稱</th>
				<th>科目代號</th>
				<th>報表起始日期</th>
				<th>報表結束日期</th>
			</tr>
			<xsl:for-each select="存款明細分類帳">
				<tr>
					<td align="center">
						<xsl:value-of select="單位名稱" />
					</td>
					<td align="center">
						<xsl:value-of select="科目代號" />
					</td>
					<td align="center">
						<xsl:value-of select="報表起始日期" />
					</td>
					<td align="center">
						<xsl:value-of select="報表結束日期" />
					</td>
				</tr>
			</xsl:for-each>
			<tr>
				<td colspan="4">
					<h4>交易資料</h4>
					<table border="0" cellspacing="0" cellpadding="0" width="100%"
						style="font-size:9pt;">
						<tr bgcolor="#9acd32">
							<th rowspan="2" nowrap="true">帳號</th>
							<th rowspan="2" nowrap="true">交易日期</th>
							<th rowspan="2" nowrap="true">存戶性質</th>
							<th rowspan="2" nowrap="true">往來狀態</th>
							<th colspan="3">交易序號</th>
							<th rowspan="2">被沖正記號</th>
							<th rowspan="2">摘要或延時營業記帳日</th>
							<th rowspan="2" nowrap="true">借方金額</th>
							<th rowspan="2" nowrap="true">貸方金額</th>
							<th rowspan="2" nowrap="true">存款餘額</th>
							<th rowspan="2">參考用未調整積數</th>
							<th rowspan="2">調整後限額內積數</th>
							<th rowspan="2">調整後限額外積數</th>
							<th rowspan="2" nowrap="true">轉入帳號</th>
						</tr>
						<tr bgcolor="#9acd32">
							<th nowrap="true">受理行</th>
							<th nowrap="true">機號</th>
							<th nowrap="true">編號</th>
						</tr>
						<xsl:for-each select="存款明細分類帳/交易資料">
							<tr>
								<td>
									<xsl:value-of select="帳號" />
									<xsl:text>　</xsl:text>
								</td>
								<td nowrap="true">
									<xsl:value-of select="交易日期" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="center">
									<xsl:value-of select="存戶性質" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="center">
									<xsl:value-of select="往來狀態" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="center" nowrap="true">
									<xsl:value-of select="交易序號/受理行" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="center" nowrap="true">
									<xsl:value-of select="交易序號/機號" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="center" nowrap="true">
									<xsl:value-of select="交易序號/編號" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="center">
									<xsl:value-of select="被沖正記號" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="right">
									<xsl:value-of select="摘要或延時營業記帳日" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="right" nowrap="true">
									<xsl:value-of select="借方金額" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="right" nowrap="true">
									<xsl:value-of select="貸方金額" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="right" nowrap="true">
									<xsl:value-of select="存款餘額" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="right" nowrap="true">
									<xsl:value-of select="參考用未調整積數" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="right" nowrap="true">
									<xsl:value-of select="調整後限額內積數" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="right" nowrap="true">
									<xsl:value-of select="調整後限額外積數" />
									<xsl:text>　</xsl:text>
								</td>
								<td align="right">
									<xsl:value-of select="轉入帳號" />
									<xsl:text>　</xsl:text>
								</td>
							</tr>
						</xsl:for-each>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>

