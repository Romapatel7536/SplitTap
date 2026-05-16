package com.roma.example.splittap.ui.expense

import android.text.format.DateUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.roma.example.splittap.R
import com.roma.example.splittap.data.model.Expense
import com.roma.example.splittap.data.model.ExpenseCategory
import com.roma.example.splittap.ui.home.AppBackground
import com.roma.example.splittap.ui.home.AppBorder
import com.roma.example.splittap.ui.home.AppGradientButton
import com.roma.example.splittap.ui.home.AppInk
import com.roma.example.splittap.ui.home.AppMuted
import com.roma.example.splittap.ui.home.AppPrimary
import com.roma.example.splittap.ui.home.AppScreenTopBar
import com.roma.example.splittap.ui.home.AppSurface
import com.roma.example.splittap.ui.home.formatCurrency
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExpensesScreen(
    expenses: List<Expense>,
    userEmail: String,
    onBack: () -> Unit,
    onAddExpense: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val horizontalPadding = if (maxWidth < 360.dp) 20.dp else 24.dp

        Column(modifier = Modifier.fillMaxSize()) {
            AppScreenTopBar(
                title = stringResource(R.string.expenses_title),
                onBack = onBack,
                trailing = {
                    FilterButton()
                }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .weight(1f)
                    .widthIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = horizontalPadding, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                AddExpenseBanner(onClick = onAddExpense)

                Text(
                    text = stringResource(R.string.expenses_total_count, expenses.size),
                    color = AppMuted,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                if (expenses.isEmpty()) {
                    ExpensesEmptyCard(onAddExpense = onAddExpense)
                } else {
                    expenses.forEach { expense ->
                        ExpenseListCard(
                            expense = expense,
                            userEmail = userEmail
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterButton() {
    Surface(
        modifier = Modifier.size(44.dp),
        shape = CircleShape,
        color = AppBackground
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(R.drawable.ic_filter_24),
                contentDescription = stringResource(R.string.expenses_cd_filter),
                tint = AppMuted,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun AddExpenseBanner(onClick: () -> Unit) {
    AppGradientButton(
        text = stringResource(R.string.home_add_expense),
        onClick = onClick,
        iconRes = R.drawable.ic_add_24,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ExpenseListCard(
    expense: Expense,
    userEmail: String
) {
    val title = expense.merchant.ifBlank {
        stringResource(R.string.home_unknown_expense)
    }
    val paidBy = if (expense.paidByEmail.equals(userEmail, ignoreCase = true)) {
        stringResource(R.string.add_expense_you)
    } else {
        expense.paidByEmail.ifBlank {
            stringResource(R.string.home_unknown_user)
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = AppInk,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stringResource(R.string.expenses_individual_group),
                        color = AppMuted,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatCurrency(expense.amount),
                        color = AppInk,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    ExpenseDateText(createdAt = expense.createdAt)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AppBorder)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.home_paid_by_format, paidBy),
                    color = AppMuted,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AppBackground
                ) {
                    Text(
                        text = expenseCategoryLabel(expense.category),
                        color = AppMuted,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun expenseCategoryLabel(category: String): String {
    return when (runCatching { ExpenseCategory.valueOf(category) }.getOrNull()) {
        ExpenseCategory.FOOD -> stringResource(R.string.add_expense_category_food)
        ExpenseCategory.BILLS -> stringResource(R.string.add_expense_category_bills)
        ExpenseCategory.DINING -> stringResource(R.string.add_expense_category_dining)
        ExpenseCategory.TRANSPORT -> stringResource(R.string.add_expense_category_transport)
        ExpenseCategory.SHOPPING -> stringResource(R.string.add_expense_category_shopping)
        ExpenseCategory.ENTERTAINMENT -> stringResource(R.string.add_expense_category_entertainment)
        ExpenseCategory.OTHER, null -> stringResource(R.string.add_expense_category_other)
    }
}

@Composable
private fun ExpensesEmptyCard(onAddExpense: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = AppSurface,
        border = BorderStroke(1.dp, AppBorder),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_receipt_24),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(R.string.home_no_expenses_yet),
                color = AppInk,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.home_no_expenses_body),
                color = AppMuted,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = stringResource(R.string.home_add_expense),
                color = AppPrimary,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onAddExpense)
            )
        }
    }
}

@Composable
private fun ExpenseDateText(createdAt: Long) {
    val formattedDate = remember(createdAt) {
        SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(createdAt))
    }
    val dateLabel = when {
        DateUtils.isToday(createdAt) -> stringResource(R.string.home_today)
        DateUtils.isToday(createdAt + DateUtils.DAY_IN_MILLIS) -> stringResource(R.string.home_yesterday)
        else -> formattedDate
    }

    Text(
        text = dateLabel,
        color = AppMuted,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1
    )
}
