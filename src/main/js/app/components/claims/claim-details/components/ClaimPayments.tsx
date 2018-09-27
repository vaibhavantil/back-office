import * as React from 'react'
import { CustomCard } from './Styles';
import { CardContent } from '@material-ui/core';

interface Props {
  payments: Array<Payment>
}

interface Payment {
  amount: {
    amount: string
    currency: string
  }
  note: string
  type: string
}

const ClaimPayments: React.SFC<Props> = ({ payments }) => (
  <CustomCard>
    <CardContent>
      <h3>Payments</h3>
      <pre>{JSON.stringify(payments, null, 2)}</pre>
    </CardContent>
  </CustomCard>
)

export { ClaimPayments }
