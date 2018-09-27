import * as React from 'react'
import { CardContent, List, ListItem } from '@material-ui/core';
import { parse, format } from 'date-fns'
import { CustomCard } from './Styles';

interface Props {
  events: Array<{
    text: string
    date: string
  }>
}

const Events: React.SFC<Props> = ({ events }) => (
  <CustomCard>
    <CardContent>
      <h3>Events</h3>
      <List>
        {events.map(event => (
          <ListItem key={event.date}>
            {format(parse(event.date), 'YYYY-MM-DD hh:mm:ss')}: {event.text}
          </ListItem>
        ))}
      </List>
    </CardContent>
  </CustomCard>
)

export { Events }
